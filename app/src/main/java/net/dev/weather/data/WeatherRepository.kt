package net.dev.weather.data

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
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

            context.settingsDataStore.data.map { settings -> settings.currentLocation }.collect { currentLocation ->
                if (currentLocation != null) {
                    //emit(LatandLong(latitude = currentLocation.latitude, longitude = currentLocation.longitude))

                    val weatherResponse = weatherServiceApi.getWeather(latitude = currentLocation.latitude, longitude = currentLocation.longitude)
                    if (weatherResponse.isSuccessful) {
                        val body = weatherResponse.body()!!
                        emit(body.toDomainModel())
                    }
                }

            }

            /*  while (true) {
                  context.settingsDataStore.data.map { settings -> settings.currentPlace }.collect { current ->
                      val weatherResponse = weatherServiceApi.getWeather(latitude = current.latitude, longitude = current.longitude)
                      if (weatherResponse.isSuccessful) {
                          val body = weatherResponse.body()!!
                          emit(body.toDomainModel())
                      }
                  }

                  delay(refreshIntervalMs)
              }*/
        }

    override val airPollutionForecast: Flow<List<AirPollutionForecast>>
        get() = flow {

            context.settingsDataStore.data.map { settings -> settings.currentLocation }.collect { currentLocation ->
                if (currentLocation != null) {
                    val airPollution = weatherServiceApi.getAirPollutionForecast(latitude = currentLocation.latitude, longitude = currentLocation.longitude)

                    if (airPollution.isSuccessful) {
                        val body = airPollution.body()!!
                        emit(body.toDomainModel())
                    }
                }
            }

            /*     while (true) {
                     context.settingsDataStore.data.map { settings -> settings.currentPlace }.collect { current ->
                         val airPollution = weatherServiceApi.getAirPollutionForecast(latitude = current.latitude, longitude = current.longitude)

                         if (airPollution.isSuccessful) {
                             val body = airPollution.body()!!
                             emit(body.toDomainModel())
                         }

                     }
                     delay(refreshIntervalMs)
                 }*/
        }

    override val airPollutionCurrent: Flow<Int>
        get() = flow {
            context.settingsDataStore.data.map { settings -> settings.currentLocation }.collect { currentLocation ->
                if (currentLocation != null) {
                    val airQuality = weatherServiceApi.getAirPollution(latitude = currentLocation.latitude, longitude = currentLocation.longitude)
                    if (airQuality.isSuccessful) {
                        val body = airQuality.body()!!
                        emit(body.list.first().main.aqi)
                    }
                }
            }


            /*while (true) {
                context.settingsDataStore.data.map { settings -> settings.currentPlace }.collect { current ->
                    val airQuality = weatherServiceApi.getAirPollution(latitude = current.latitude, longitude = current.longitude)
                    if (airQuality.isSuccessful) {
                        val body = airQuality.body()!!
                        emit(body.list.first().main.aqi)
                    }
                }
                delay(refreshIntervalMs)
            }*/
        }

    companion object {
        private const val refreshIntervalMs = 1000 * 60L
    }
}


