package net.dev.weather.repositories

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import net.dev.weather.api.LocationServiceApi
import net.dev.weather.api.WeatherServiceApi
import net.dev.weather.data.*
import javax.inject.Inject
import javax.inject.Singleton

interface LocationRepository {
    val currentPlace: Flow<Place>

    val currentDevicePlace: Flow<Place>

    fun getSuggestions(input: String): Flow<List<Suggestion>>

    suspend fun getLocationFromGoogle(placeId: String): LatandLong
}

@Singleton
class LocationRepositoryImpl @Inject constructor(
    private val weatherServiceApi: WeatherServiceApi,
    private val locationServiceApi: LocationServiceApi,
    private val settingsRepository: SettingsRepository
) : LocationRepository {

    override val currentPlace: Flow<Place>
        get() = flow {
            settingsRepository.currentMode.collect {
                when (it) {
                    PlaceMode.DEVICE_LOCATION -> emit(currentDevicePlace.first())
                    PlaceMode.SEARCH -> emit(settingsRepository.currentPlace.first())
                    PlaceMode.FAVORITES -> emit(settingsRepository.currentPlace.first())
                }
            }
        }

    override val currentDevicePlace: Flow<Place>
        get() = flow {
            settingsRepository.currentDeviceLocation.collect {
                it ?: return@collect

                val reverseLocationResponse = weatherServiceApi.getReverseLocation(it.latitude, it.longitude)
                val name = reverseLocationResponse.body()?.first()?.name ?: "Unknown"
                val value = Place(name, deviceCurrentLocation.id, deviceCurrentLocation.description, it.latitude, it.longitude)

                Log.d("LocationRepository", "currentDevicePlace: $value")

                emit(value)
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

    override suspend fun getLocationFromGoogle(placeId: String): LatandLong {
        if (placeId == deviceCurrentLocation.id)
            return LatandLong(0.0, 0.0)

        val latLongResponse = locationServiceApi.getLatLongFromGoogle(placeId)
        return if (latLongResponse.isSuccessful) {
            val body = latLongResponse.body()!!
            LatandLong(body.results.first().geometry.location.lat, body.results.first().geometry.location.lng)
        } else
            LatandLong(0.0, 0.0)
    }
}