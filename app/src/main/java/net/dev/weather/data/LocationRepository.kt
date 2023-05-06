package net.dev.weather.data

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import net.dev.weather.api.LocationServiceApi
import net.dev.weather.api.WeatherServiceApi
import net.dev.weather.settingsDataStore
import javax.inject.Inject
import javax.inject.Singleton

interface LocationRepository {
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

    override val locationName: Flow<String>
        get() = flow {
            context.settingsDataStore.data.map { settings -> settings.currentPlace }.collect { currentPlace ->
                if (currentPlace != null) {
                    emit(currentPlace.name)
                } else {
                    //TODO: tutaj powinny być współrzędne na podstawie lokalizacji urządzenia
                    val reverseLocationResponse = weatherServiceApi.getReverseLocation()
                    val location = reverseLocationResponse.body()?.first()?.name ?: "Unknown"
                    emit(location)
                }
            }
        }
    override val savedPlaces: Flow<List<Place>>
        get() = flow {
            context.settingsDataStore.data.map { settings ->
                settings.placesList.map { place ->
                    Place(place.name, place.id, place.description, place.latitude, place.longitude)
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
            val p = currentSettings.placesList.find { it.id == suggestion.id }
            if (p != null) {
                currentSettings.toBuilder().clearPlaces().addAllPlaces(currentSettings.placesList.filter { it.id != suggestion.id }).build()
            } else {
                currentSettings.toBuilder().addPlaces(buildPlace(suggestion)).build()
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
            currentSettings.toBuilder().clearPlaces().addAllPlaces(currentSettings.placesList.filter { it.id != placeId }).build()
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
            currentSettings.toBuilder().clearPlaces().build()
        }
    }

    private suspend fun buildPlace(suggestion: Suggestion): net.dev.weather.Place {
        val location = getLocationFromGoogle(suggestion.id)

        val place = net.dev.weather.Place.newBuilder().also {
            it.id = suggestion.id
            it.name = suggestion.name
            it.description = suggestion.description
            it.latitude = location.first
            it.longitude = location.second
        }.build()

        return place
    }

    private suspend fun buildPlace(place: Place): net.dev.weather.Place {
        val location = getLocationFromGoogle(place.id)

        val place2 = net.dev.weather.Place.newBuilder().also {
            it.id = place.id
            it.name = place.name
            it.description = place.description
            it.latitude = location.first
            it.longitude = location.second
        }.build()

        return place2
    }
}


