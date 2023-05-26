package net.dev.weather.repositories

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import net.dev.weather.api.WeatherServiceApi
import net.dev.weather.data.AirPollutionForecast
import net.dev.weather.data.Weather
import net.dev.weather.data.toDomainModel
import javax.inject.Inject
import javax.inject.Singleton

interface WeatherRepository {
    val weather: Flow<Weather>

    val airPollutionForecast: Flow<List<AirPollutionForecast>>

    val airPollutionCurrent: Flow<Int>
}

@Singleton
class NetworkRepository @Inject constructor(private val weatherServiceApi: WeatherServiceApi, private val locationRepository: LocationRepository) :
    WeatherRepository {
    override val weather: Flow<Weather>
        get() = flow {
            locationRepository.currentPlace.collect { currentPlace ->
                val weatherResponse = weatherServiceApi.getWeather(latitude = currentPlace.latitude, longitude = currentPlace.longitude)
                if (weatherResponse.isSuccessful) {
                    val body = weatherResponse.body()!!
                    emit(body.toDomainModel())
                }
            }
        }

    override val airPollutionForecast: Flow<List<AirPollutionForecast>>
        get() = flow {
            locationRepository.currentPlace.collect { currentPlace ->
                val airPollution = weatherServiceApi.getAirPollutionForecast(latitude = currentPlace.latitude, longitude = currentPlace.longitude)

                if (airPollution.isSuccessful) {
                    val body = airPollution.body()!!
                    emit(body.toDomainModel())
                }
            }
        }

    override val airPollutionCurrent: Flow<Int>
        get() = flow {
            locationRepository.currentPlace.collect { currentPlace ->
                val airQuality = weatherServiceApi.getAirPollution(latitude = currentPlace.latitude, longitude = currentPlace.longitude)
                if (airQuality.isSuccessful) {
                    val body = airQuality.body()!!
                    emit(body.list.first().main.aqi)
                }
            }
        }
}