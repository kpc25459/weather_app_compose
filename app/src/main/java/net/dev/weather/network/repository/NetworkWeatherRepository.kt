package net.dev.weather.network.repository

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import net.dev.weather.data.model.AirPollutionForecast
import net.dev.weather.data.model.Weather
import net.dev.weather.data.model.WeatherDays
import net.dev.weather.data.repository.PlaceRepository
import net.dev.weather.data.repository.WeatherRepository
import net.dev.weather.network.api.WeatherServiceApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkWeatherRepository @Inject constructor(
    private val weatherServiceApi: WeatherServiceApi,
    private val placeRepository: PlaceRepository
) : WeatherRepository {
    /*    override val weather: Flow<WeatherDays>
            get() = flow {
                placeRepository.currentPlace.collect { currentPlace ->
                    val weatherResponse = weatherServiceApi.getWeather(latitude = currentPlace.latitude, longitude = currentPlace.longitude)
                    if (weatherResponse.isSuccessful) {
                        val body = weatherResponse.body()!!
                        emit(body.mapToWeatherDays())
                    }
                }
            }*/

    override suspend fun weatherFor(latitude: Double, longitude: Double): Weather = coroutineScope {

        //TODO: obsłużyć wyjątki

        val aqiDeferred = async {
            val airPollutionResponse = weatherServiceApi.getAirPollution(latitude = latitude, longitude = longitude)
            if (airPollutionResponse.isSuccessful) {
                val airQualityBody = airPollutionResponse.body()!!

                return@async airQualityBody.list.first().main.aqi
            } else {
                return@async null
            }
        }

        val weatherDeferred = async {
            val weatherResponse = weatherServiceApi.getWeather(latitude = latitude, longitude = longitude)
            if (weatherResponse.isSuccessful) {
                val body = weatherResponse.body()!!
                return@async body.mapToWeatherDays()
            } else {
                return@async null
            }
        }

        val aqi: Int = aqiDeferred.await() ?: throw Exception("aqiDeferred is null")
        val weather: WeatherDays = weatherDeferred.await() ?: throw Exception("weatherDeferred is null")

        return@coroutineScope Weather(weather.current, weather.daily, weather.hourly, aqi)
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