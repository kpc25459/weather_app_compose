package net.dev.weather.network.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import net.dev.weather.data.model.AirPollutionForecast
import net.dev.weather.data.model.Weather
import net.dev.weather.data.repository.PlaceRepository
import net.dev.weather.data.repository.WeatherRepository
import net.dev.weather.network.api.WeatherServiceApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkWeatherRepository @Inject constructor(private val weatherServiceApi: WeatherServiceApi, private val placeRepository: PlaceRepository) : WeatherRepository {
    override val weather: Flow<Weather>
        get() = flow {
            placeRepository.currentPlace.collect { currentPlace ->
                val weatherResponse = weatherServiceApi.getWeather(latitude = currentPlace.latitude, longitude = currentPlace.longitude)
                if (weatherResponse.isSuccessful) {
                    val body = weatherResponse.body()!!
                    emit(body.mapToWeather())
                }
            }
        }

    override val airPollutionForecast: Flow<List<AirPollutionForecast>>
        get() = flow {
            placeRepository.currentPlace.collect { currentPlace ->
                val airPollution = weatherServiceApi.getAirPollutionForecast(latitude = currentPlace.latitude, longitude = currentPlace.longitude)

                if (airPollution.isSuccessful) {
                    val body = airPollution.body()!!
                    emit(body.mapToAirPollutionForecast())
                }
            }
        }

    override val airPollutionCurrent: Flow<Int>
        get() = flow {
            placeRepository.currentPlace.collect { currentPlace ->
                val airQuality = weatherServiceApi.getAirPollution(latitude = currentPlace.latitude, longitude = currentPlace.longitude)
                if (airQuality.isSuccessful) {
                    val body = airQuality.body()!!
                    emit(body.list.first().main.aqi)
                }
            }
        }
}