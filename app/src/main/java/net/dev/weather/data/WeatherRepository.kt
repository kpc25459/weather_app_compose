package net.dev.weather.data

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import net.dev.weather.api.WeatherServiceApi
import net.dev.weather.settingsDataStore
import javax.inject.Inject
import javax.inject.Singleton

interface WeatherRepository {
    val weather: Flow<Weather>

    val airPollutionForecast: Flow<List<AirPollutionForecast>>

    val airPollutionCurrent: Flow<Int>
}

@Singleton
class NetworkRepository @Inject constructor(private val weatherServiceApi: WeatherServiceApi, @ApplicationContext private val context: Context) : WeatherRepository {
    override val weather: Flow<Weather>
        get() = flow {

            context.settingsDataStore.data.map { settings -> settings.currentPlace }.collect { currentPlace ->
                if (currentPlace != null) {
                    val weatherResponse = weatherServiceApi.getWeather(latitude = currentPlace.location.latitude, longitude = currentPlace.location.longitude)
                    if (weatherResponse.isSuccessful) {
                        val body = weatherResponse.body()!!
                        emit(body.toDomainModel())
                    }
                }
            }
        }

    override val airPollutionForecast: Flow<List<AirPollutionForecast>>
        get() = flow {

            context.settingsDataStore.data.map { settings -> settings.currentPlace }.collect { currentPlace ->
                if (currentPlace != null) {
                    val airPollution = weatherServiceApi.getAirPollutionForecast(latitude = currentPlace.location.latitude, longitude = currentPlace.location.longitude)

                    if (airPollution.isSuccessful) {
                        val body = airPollution.body()!!
                        emit(body.toDomainModel())
                    }
                }
            }
        }

    override val airPollutionCurrent: Flow<Int>
        get() = flow {
            context.settingsDataStore.data.map { settings -> settings.currentPlace }.collect { currentPlace ->
                if (currentPlace != null) {
                    val airQuality = weatherServiceApi.getAirPollution(latitude = currentPlace.location.latitude, longitude = currentPlace.location.longitude)
                    if (airQuality.isSuccessful) {
                        val body = airQuality.body()!!
                        emit(body.list.first().main.aqi)
                    }
                }
            }
        }
}


