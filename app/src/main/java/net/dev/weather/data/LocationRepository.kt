package net.dev.weather.data

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import net.dev.weather.api.LocationServiceApi
import net.dev.weather.api.WeatherServiceApi
import javax.inject.Inject
import javax.inject.Singleton

interface LocationRepository {
    val location: Flow<String>

    val savedPlaces: Flow<List<Place>>

    fun getSuggestions(input: String): Flow<List<Pair<String, String>>>
    //fun getLatLongFromGoogle(placeId: String): Flow<CurrentLocation>
}

@Singleton
class LocationRepositoryImpl @Inject constructor(private val weatherServiceApi: WeatherServiceApi, private val locationServiceApi: LocationServiceApi) : LocationRepository {

    override val location: Flow<String>
        get() = flow {
            val reverseLocationResponse = weatherServiceApi.getReverseLocation()
            val location = reverseLocationResponse.body()?.first()?.name ?: "Unknown"
            emit(location)
        }
    override val savedPlaces: Flow<List<Place>>
        get() = flow {
            emit(listOf(Place("London", "London1"), Place("Paris", "Paris1"), Place("New York", "New York1")))
        }

    override fun getSuggestions(input: String): Flow<List<Pair<String, String>>> = flow {
        val suggestionsResponse = locationServiceApi.getSuggestions(input)
        if (suggestionsResponse.isSuccessful) {
            val body = suggestionsResponse.body()!!
            val suggestions = body.predictions.map { prediction ->
                val placeId = prediction.place_id
                val description = prediction.description
                Pair(description, placeId)
            }
            emit(suggestions)
        }
    }

    /*   override fun getLatLongFromGoogle(placeId: String): Flow<CurrentLocation> =
           flow {
               val latLongResponse = locationServiceApi.getLatLongFromGoogle(placeId)
               if (latLongResponse.isSuccessful) {
                   val body = latLongResponse.body()!!
                   emit(body.first())
               }
           }*/

    companion object {

    }
}


