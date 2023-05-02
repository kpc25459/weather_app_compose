package net.dev.weather.data

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import net.dev.weather.api.LocationServiceApi
import net.dev.weather.api.WeatherServiceApi
import net.dev.weather.settingsDataStore
import javax.inject.Inject
import javax.inject.Singleton

interface LocationRepository {
    val location: Flow<String>

    val savedPlaces: Flow<List<Place>>

    fun getSuggestions(input: String): Flow<List<Pair<String, String>>>
    suspend fun addPlaces()
    //fun getLatLongFromGoogle(placeId: String): Flow<CurrentLocation>
}

@Singleton
class LocationRepositoryImpl @Inject constructor(
    private val weatherServiceApi: WeatherServiceApi,
    private val locationServiceApi: LocationServiceApi,
    @ApplicationContext private val context: Context
) : LocationRepository {

    override val location: Flow<String>
        get() = flow {
            val reverseLocationResponse = weatherServiceApi.getReverseLocation()
            val location = reverseLocationResponse.body()?.first()?.name ?: "Unknown"
            emit(location)
        }
    override val savedPlaces: Flow<List<Place>>
        get() = flow {
            context.settingsDataStore.data.map { settings ->
                settings.placesList.map { place ->
                    Place(place.name, place.id)
                }
            }.collect { places ->
                emit(places)
            }
            //emit(listOf(Place("London", "London1"), Place("Paris", "Paris1"), Place("New York", "New York1")))
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

    override suspend fun addPlaces() {
        context.settingsDataStore.updateData { currentSettings ->
            currentSettings.toBuilder()
                //.addAllPlaces( places)
                .addAllPlaces(
                    listOf(
                        net.dev.weather.Place.newBuilder().also {
                            it.name = "Poznań"
                            it.id = "1"
                        }.build(),
                        net.dev.weather.Place.newBuilder().also {
                            it.name = "Warszawa"
                            it.id = "2"
                        }.build(),
                        net.dev.weather.Place.newBuilder().also {
                            it.name = "Wrocław"
                            it.id = "3"
                        }.build()
                    )
                )
                .build()
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


