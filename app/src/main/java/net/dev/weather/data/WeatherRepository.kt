package net.dev.weather.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import net.dev.weather.api.OneCallResponse
import net.dev.weather.api.WeatherDailyResponse
import net.dev.weather.api.WeatherHourlyResponse
import net.dev.weather.api.WeatherServiceApi
import net.dev.weather.toHumanFromDegrees
import kotlin.math.roundToInt


interface WeatherRepository {
    val location: Flow<String>
    val currentWeather: Flow<WeatherHourlyResponse>
    val dailyWeather: Flow<List<WeatherDaily>>

    /*  val hourlyWeather: Flow<List<WeatherHourlyResponse>>

      val airPollution: Flow<OneCallResponse>*/

    val weather: Flow<OneCallResponse>
}

class NetworkRepository(private val weatherServiceApi: WeatherServiceApi) : WeatherRepository {

    override val weather: Flow<OneCallResponse>
        get() = flow {
            val weather = weatherServiceApi.getWeather()
            if (weather.isSuccessful) {
                emit(weather.body()!!)
            }
        }


    override val location: Flow<String>
        get() = flow {
            val reverseLocationResponse = weatherServiceApi.getReverseLocation()
            val location = reverseLocationResponse.body()?.first()?.name ?: "Unknown"
            emit(location)
        }

    override val currentWeather: Flow<WeatherHourlyResponse>
        get() = flow {
            val weather = weatherServiceApi.getWeather()
            if (weather.isSuccessful) {
                emit(weather.body()!!.current)
            }
        }

    override val dailyWeather: Flow<List<WeatherDaily>>
        get() = flow {
            val weather = weatherServiceApi.getWeather()
            if (weather.isSuccessful) {

                val body = weather.body()!!

                val timeZone = TimeZone.of(body.timezone)

                emit(body.daily.map {
                    WeatherDaily(
                        Instant.fromEpochSeconds(it.dt.toLong()).toLocalDateTime(timeZone),
                        description = it.weather.first().description,
                        sunrise = Instant.fromEpochSeconds(it.sunrise.toLong()).toLocalDateTime(timeZone).time.toString().substringBeforeLast(":"),
                        sunset = Instant.fromEpochSeconds(it.sunset.toLong()).toLocalDateTime(timeZone).time.toString().substringBeforeLast(":"),
                        temp = "${it.temp.day}°C / ${it.temp.night}°C",
                        pressure = "${it.pressure} hPa",
                        humidity = "${it.humidity} %",
                        wind = "${((it.wind_speed * 3.6 * 100) / 100).roundToInt()} km/h ${toHumanFromDegrees(it.wind_deg)}",
                        rain = "${it.rain.roundToInt()} mm/24h",
                        uvi = it.uvi.roundToInt().toString(),
                        icon = it.weather[0].icon
                    )
                })
            }
        }

    suspend fun getDailyForecastWeather(): Flow<List<WeatherDailyResponse>> {
        val weather = weatherServiceApi.getWeather()

        val body = weather.body() ?: throw Exception("No data")

        return flowOf(body.daily.takeLast(7))
    }

    suspend fun getHourlyForecastWeather(): Flow<List<WeatherHourlyResponse>> {
        val weather = weatherServiceApi.getWeather()

        val body = weather.body() ?: throw Exception("No data")

        return flowOf(body.hourly.take(24))
    }

    suspend fun getAirQuality(): Flow<Int> {

        val airQuality = weatherServiceApi.getAirPollution()

        val body = airQuality.body() ?: return flowOf(-1) //throw Exception("No data")

        val aqi = body.list.first().main.aqi

        return flowOf(aqi)
    }


}