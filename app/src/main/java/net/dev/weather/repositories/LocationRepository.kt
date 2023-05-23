package net.dev.weather.repositories

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import net.dev.weather.api.LocationServiceApi
import net.dev.weather.api.WeatherServiceApi
import net.dev.weather.data.LatandLong
import net.dev.weather.data.Suggestion
import net.dev.weather.ui.places.currentLocation
import javax.inject.Inject
import javax.inject.Singleton

interface LocationRepository {
    val currentDeviceLocationName: Flow<String>

    fun getSuggestions(input: String): Flow<List<Suggestion>>

    suspend fun getLocationFromGoogle(placeId: String): LatandLong
}

@Singleton
class LocationRepositoryImpl @Inject constructor(
    private val weatherServiceApi: WeatherServiceApi,
    private val locationServiceApi: LocationServiceApi,
    private val settingsRepository: SettingsRepository
) : LocationRepository {


    override val currentDeviceLocationName: Flow<String>
        get() = flow {
            settingsRepository.currentDeviceLocation.collect {
                it ?: return@collect

                val reverseLocationResponse = weatherServiceApi.getReverseLocation(it.latitude, it.longitude)
                val location = reverseLocationResponse.body()?.first()?.name ?: "Unknown"
                emit(location)
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
        if (placeId == currentLocation.id)
            return LatandLong(0.0, 0.0)

        val latLongResponse = locationServiceApi.getLatLongFromGoogle(placeId)
        return if (latLongResponse.isSuccessful) {
            val body = latLongResponse.body()!!
            LatandLong(body.results.first().geometry.location.lat, body.results.first().geometry.location.lng)
        } else
            LatandLong(0.0, 0.0)
    }
}