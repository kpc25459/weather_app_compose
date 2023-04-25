package net.dev.weather.data

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import net.dev.weather.api.WeatherServiceApi
import javax.inject.Inject
import javax.inject.Singleton

interface WeatherRepository {
    val weather: Flow<Weather>

    val airPollutionForecast: Flow<List<AirPollutionForecast>>

    val airPollutionCurrent: Flow<Int>
}

@Singleton
class NetworkRepository @Inject constructor(private val weatherServiceApi: WeatherServiceApi) : WeatherRepository {
    override val weather: Flow<Weather>
        get() = flow {
            while (true) {
                val weatherResponse = weatherServiceApi.getWeather()
                if (weatherResponse.isSuccessful) {
                    val body = weatherResponse.body()!!
                    emit(body.toDomainModel())
                }
                delay(refreshIntervalMs)
            }
        }

    override val airPollutionForecast: Flow<List<AirPollutionForecast>>
        get() = flow {
            while (true) {
                val airPollution = weatherServiceApi.getAirPollutionForecast()

                if (airPollution.isSuccessful) {
                    val body = airPollution.body()!!
                    emit(body.toDomainModel())
                }
                delay(refreshIntervalMs)
            }
        }

    override val airPollutionCurrent: Flow<Int>
        get() = flow {
            while (true) {
                val airQuality = weatherServiceApi.getAirPollution()
                if (airQuality.isSuccessful) {
                    val body = airQuality.body()!!
                    emit(body.list.first().main.aqi);
                }
                delay(refreshIntervalMs)
            }
        }

    companion object{
        private const val refreshIntervalMs = 1000 * 60L
    }
}


