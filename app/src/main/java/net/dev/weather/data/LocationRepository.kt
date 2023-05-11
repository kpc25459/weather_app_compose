package net.dev.weather.data

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import net.dev.weather.Location
import net.dev.weather.api.LocationServiceApi
import net.dev.weather.api.WeatherServiceApi
import net.dev.weather.settingsDataStore
import javax.inject.Inject
import javax.inject.Singleton

interface LocationRepository {

    val location: Flow<LatandLong>

    val locationName: Flow<String>

    val savedPlaces: Flow<List<Place>>

    fun getSuggestions(input: String): Flow<List<Suggestion>>

    suspend fun clearPlaces()

    suspend fun toggleFavorite(suggestion: Suggestion)
    suspend fun removeFromFavorites(placeId: String)
    suspend fun setCurrentLocation(suggestion: Suggestion)
    suspend fun setCurrentLocation(place: Place)
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
                    emit(LatandLong(latitude = currentLocation.latitude, longitude = currentLocation.longitude))
                } else {
                    emit(LatandLong(latitude = 0.0, longitude = 0.0))
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
                    Place(place.name, place.id, place.description,/* place.location.latitude, place.location.longitude*/ 0.0, 0.0)
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
                currentSettings.toBuilder().addFavorites(buildPlace(suggestion)).build()
            }
        }
    }

    private suspend fun getLocationFromGoogle(placeId: String): Pair<Double, Double> {
        val latLongResponse = locationServiceApi.getLatLongFromGoogle(placeId)
        return if (latLongResponse.isSuccessful) {
            val body = latLongResponse.body()!!
            body.results.first().geometry.location.lat to body.results.first().geometry.location.lng
        } else
            0.0 to 0.0
    }

    override suspend fun removeFromFavorites(placeId: String) {
        context.settingsDataStore.updateData { currentSettings ->
            currentSettings.toBuilder().clearFavorites().addAllFavorites(currentSettings.favoritesList.filter { it.id != placeId }).build()
        }
    }

    override suspend fun setCurrentLocation(suggestion: Suggestion) {

        val place = buildPlace(suggestion)

        context.settingsDataStore.updateData { currentSettings ->
            currentSettings.toBuilder().setCurrentPlace(place).build()
        }
    }

    override suspend fun setCurrentLocation(place: Place) {
        val place2 = buildPlace(place)

        context.settingsDataStore.updateData { currentSettings ->
            currentSettings.toBuilder().setCurrentPlace(place2).build()
        }
    }

    override suspend fun clearPlaces() {
        context.settingsDataStore.updateData { currentSettings ->
            currentSettings.toBuilder().clearFavorites().build()
        }
    }

    private suspend fun buildPlace(suggestion: Suggestion): net.dev.weather.Place {
        val location = getLocationFromGoogle(suggestion.id)

        val place = net.dev.weather.Place.newBuilder().also {
            it.id = suggestion.id
            it.name = suggestion.name
            it.description = suggestion.description
            it.location = Location.newBuilder().also { builder ->
                builder.latitude = location.first
                builder.longitude = location.second
            }.build()
        }.build()

        return place
    }

    private suspend fun buildPlace(place: Place): net.dev.weather.Place {
        val location = getLocationFromGoogle(place.id)

        val place2 = net.dev.weather.Place.newBuilder().also {
            it.id = place.id
            it.name = place.name
            it.description = place.description
            it.location = Location.newBuilder().also { builder ->
                builder.latitude = location.first
                builder.longitude = location.second
            }.build()
        }.build()

        return place2
    }
    /*
        companion object {
            private const val refreshIntervalMs = 1000 * 60L
        }*/
}


