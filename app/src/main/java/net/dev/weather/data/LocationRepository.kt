package net.dev.weather.data

import android.content.Context
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import net.dev.weather.Location
import net.dev.weather.api.LocationServiceApi
import net.dev.weather.api.WeatherServiceApi
import net.dev.weather.settingsDataStore
import net.dev.weather.ui.places.currentLocation
import javax.inject.Inject
import javax.inject.Singleton

interface LocationRepository {

    val location: Flow<LatandLong>

    val locationName: Flow<String>

    val currentPlace: Flow<Place>

    val savedPlaces: Flow<List<Place>>

    fun getSuggestions(input: String): Flow<List<Suggestion>>

    suspend fun clearPlaces()

    suspend fun toggleFavorite(suggestion: Suggestion)
    suspend fun removeFromFavorites(placeId: String)
    suspend fun setCurrentPlace(suggestion: Suggestion)
    suspend fun setCurrentPlace(place: Place)
}

@Singleton
class LocationRepositoryImpl @Inject constructor(
    private val weatherServiceApi: WeatherServiceApi,
    private val locationServiceApi: LocationServiceApi,
    @ApplicationContext private val context: Context
) : LocationRepository {

    override val location: Flow<LatandLong>
        get() = flow {
            context.settingsDataStore.data.map { settings -> settings.currentLocation }.collect { currentLocation ->
                if (currentLocation != null) {
                    Log.d("LocationRepository", "emit location: $currentLocation")
                    emit(LatandLong(latitude = currentLocation.latitude, longitude = currentLocation.longitude))
                } else {
                    emit(LatandLong(latitude = 0.0, longitude = 0.0))
                }
            }
        }

    override val currentPlace: Flow<Place>
        get() = flow {
            context.settingsDataStore.data.map { settings -> settings.currentPlace }.collect { currentPlace ->
                currentPlace?.let {
                    emit(
                        Place(
                            name = currentPlace.name,
                            id = currentPlace.id,
                            description = currentPlace.description,
                            latitude = currentPlace.location.latitude,
                            longitude = currentPlace.location.longitude
                        )
                    )
                }
            }
        }

    override val locationName: Flow<String>
        get() = flow {

            location.collect {
                val reverseLocationResponse = weatherServiceApi.getReverseLocation(it.latitude, it.longitude)
                val location = reverseLocationResponse.body()?.first()?.name ?: "Unknown"
                emit(location)
            }


            /*  context.settingsDataStore.data.map { settings -> settings.currentPlace }.collect { currentPlace ->
                  if (currentPlace != null) {
                      emit(currentPlace.name)
                  } else {


                      //TODO: tutaj powinny być współrzędne na podstawie lokalizacji urządzenia
                      val reverseLocationResponse = weatherServiceApi.getReverseLocation()
                      val location = reverseLocationResponse.body()?.first()?.name ?: "Unknown"
                      emit(location)
                  }
              }*/
        }

    override val savedPlaces: Flow<List<Place>>
        get() = flow {
            context.settingsDataStore.data.map { settings ->
                settings.favoritesList.map { place ->
                    Place(place.name, place.id, place.description, place.location.latitude, place.location.longitude)
                }
            }.collect { places ->
                emit(places)
            }
        }

    override fun getSuggestions(input: String): Flow<List<Suggestion>> = flow {
        val suggestionsResponse = locationServiceApi.getSuggestions(input)
        if (suggestionsResponse.isSuccessful) {
            val body = suggestionsResponse.body()!!
            val suggestions = body.predictions.map { prediction ->
                val placeId = prediction.place_id
                val name = prediction.structured_formatting.main_text
                val description = prediction.structured_formatting.secondary_text

                Suggestion(name, placeId, description)
            }
            emit(suggestions)
        }
    }

    override suspend fun toggleFavorite(suggestion: Suggestion) {
        context.settingsDataStore.updateData { currentSettings ->
            val p = currentSettings.favoritesList.find { it.id == suggestion.id }
            if (p != null) {
                currentSettings.toBuilder().clearFavorites().addAllFavorites(currentSettings.favoritesList.filter { it.id != suggestion.id }).build()
            } else {
                currentSettings.toBuilder().addFavorites(buildPlaceFromSuggestion(suggestion)).build()
            }
        }
    }

    private suspend fun getLocationFromGoogle(placeId: String): LatandLong {
        if (placeId == currentLocation.id)
            return LatandLong(0.0, 0.0)

        val latLongResponse = locationServiceApi.getLatLongFromGoogle(placeId)
        return if (latLongResponse.isSuccessful) {
            val body = latLongResponse.body()!!
            LatandLong(body.results.first().geometry.location.lat, body.results.first().geometry.location.lng)
        } else
            LatandLong(0.0, 0.0)
    }

    override suspend fun removeFromFavorites(placeId: String) {
        context.settingsDataStore.updateData { currentSettings ->
            currentSettings.toBuilder().clearFavorites().addAllFavorites(currentSettings.favoritesList.filter { it.id != placeId }).build()
        }
    }

    override suspend fun setCurrentPlace(suggestion: Suggestion) {

        val place = buildPlaceFromSuggestion(suggestion)

        context.settingsDataStore.updateData { currentSettings ->
            currentSettings.toBuilder().setCurrentPlace(place).build()
            //currentSettings.toBuilder().setCurrentLocation(place.location).build()
        }
    }

    override suspend fun setCurrentPlace(place: Place) {
        val place2 = buildPlaceFromSuggestion(if (place.id == currentLocation.id) buildFromCurrentLocation() else place)

        context.settingsDataStore.updateData { currentSettings ->
            currentSettings.toBuilder().setCurrentPlace(place2).build()
            //currentSettings.toBuilder().setCurrentLocation(place2.location).build()
        }
    }

    private suspend fun buildFromCurrentLocation(): Place {
        val lastLocation = context.settingsDataStore.data.map { settings -> settings.currentLocation }.first()

        val reverseLocationResponse = weatherServiceApi.getReverseLocation(lastLocation.latitude, lastLocation.longitude)
        val name = reverseLocationResponse.body()?.first()?.name ?: "Unknown"
        return Place(name, currentLocation.id, currentLocation.description, lastLocation.latitude, lastLocation.longitude)
    }

    override suspend fun clearPlaces() {
        context.settingsDataStore.updateData { currentSettings ->
            currentSettings.toBuilder().clearFavorites().build()
        }
    }

    private suspend fun buildPlaceFromSuggestion(suggestion: Suggestion): net.dev.weather.Place {
        val location = getLocationFromGoogle(suggestion.id)

        val place = net.dev.weather.Place.newBuilder().also {
            it.id = suggestion.id
            it.name = suggestion.name
            it.description = suggestion.description
            it.location = Location.newBuilder().also { builder ->
                builder.latitude = location.latitude
                builder.longitude = location.longitude
            }.build()
        }.build()

        return place
    }

    private suspend fun buildPlaceFromSuggestion(place: Place): net.dev.weather.Place {
        val location = getLocationFromGoogle(place.id)

        val place2 = net.dev.weather.Place.newBuilder().also {
            it.id = place.id
            it.name = place.name
            it.description = place.description
            it.location = Location.newBuilder().also { builder ->
                builder.latitude = location.latitude
                builder.longitude = location.longitude
            }.build()
        }.build()

        return place2
    }
    /*
        companion object {
            private const val refreshIntervalMs = 1000 * 60L
        }*/
}


