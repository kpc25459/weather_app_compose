package net.dev.weather.data.repository

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import net.dev.weather.api.LocationServiceApi
import net.dev.weather.data.model.LatandLong
import net.dev.weather.data.model.Place
import net.dev.weather.data.model.PlaceMode
import net.dev.weather.data.model.Suggestion
import net.dev.weather.data.model.deviceCurrentLocation
import net.dev.weather.network.api.WeatherServiceApi
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
            settingsRepository.userData.collect {
                when (it.currentMode) {
                    PlaceMode.DEVICE_LOCATION -> emit(currentDevicePlace.first())
                    PlaceMode.SEARCH -> it.currentPlace?.let { it1 -> emit(it1) }
                    PlaceMode.FAVORITES -> it.currentPlace?.let { it1 -> emit(it1) }
                }
            }
        }

    override val currentDevicePlace: Flow<Place>
        get() = flow {
            settingsRepository.userData.collect{
                val currentDeviceLocation = it.currentDeviceLocation ?: return@collect

                val reverseLocationResponse = weatherServiceApi.getReverseLocation(currentDeviceLocation.latitude, currentDeviceLocation.longitude)
                val name = reverseLocationResponse.body()?.first()?.name ?: "Unknown"
                val value = Place(name, deviceCurrentLocation.id, deviceCurrentLocation.description, currentDeviceLocation.latitude, currentDeviceLocation.longitude)

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