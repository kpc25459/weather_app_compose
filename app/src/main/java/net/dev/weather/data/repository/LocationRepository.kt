package net.dev.weather.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import net.dev.weather.api.LocationServiceApi
import net.dev.weather.data.model.LatandLong
import net.dev.weather.data.model.Suggestion
import net.dev.weather.data.model.deviceCurrentLocationPlace
import javax.inject.Inject
import javax.inject.Singleton

interface LocationRepository {
    fun getSuggestions(input: String): Flow<List<Suggestion>>

    suspend fun getLocationFromGoogle(placeId: String): LatandLong
}

@Singleton
class LocationRepositoryImpl @Inject constructor(
    private val locationServiceApi: LocationServiceApi,
) : LocationRepository {

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
        if (placeId == deviceCurrentLocationPlace.id)
            return LatandLong(0.0, 0.0)

        val latLongResponse = locationServiceApi.getLatLongFromGoogle(placeId)
        return if (latLongResponse.isSuccessful) {
            val body = latLongResponse.body()!!
            LatandLong(body.results.first().geometry.location.lat, body.results.first().geometry.location.lng)
        } else
            LatandLong(0.0, 0.0)
    }
}