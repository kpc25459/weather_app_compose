package net.dev.weather.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import net.dev.weather.R
import net.dev.weather.api.OneCallResponse
import net.dev.weather.api.WeatherDailyResponse
import net.dev.weather.api.WeatherHourlyResponse
import net.dev.weather.api.WeatherServiceApi
import net.dev.weather.toHumanFromDegrees
import net.dev.weather.ui.currentWeather.CurrentWeatherViewModel
import kotlin.math.roundToInt


interface WeatherRepository {
    //val weather: Flow<OneCallResponse>

    val location: Flow<String>
    val currentWeather: Flow<WeatherHourly>
    val dailyWeather: Flow<List<WeatherDaily>>

    /*  val hourlyWeather: Flow<List<WeatherHourlyResponse>>

      val airPollution: Flow<OneCallResponse>*/


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

    override val currentWeather: Flow<WeatherHourly>
        get() = flow {
            val weather = weatherServiceApi.getWeather()
            if (weather.isSuccessful) {
                val body = weather.body()!!
                val current = body.current

                val timeZone = TimeZone.of(body.timezone)

                val currentWeather = CurrentWeather(
                    dt = Instant.fromEpochSeconds(current.dt.toLong()).toLocalDateTime(timeZone),
                    sunrise = Instant.fromEpochSeconds(current.sunrise.toLong()).toLocalDateTime(timeZone).time.toString().substringBeforeLast(":"),
                    sunset = Instant.fromEpochSeconds(current.sunset.toLong()).toLocalDateTime(timeZone).time.toString().substringBeforeLast(":"),
                    temp = current.temp.roundToInt(),
                    feels_like = "${current.feels_like.roundToInt()} °",
                    pressure = "${current.pressure} hPa",
                    humidity = "${current.humidity} %",
                    dew_point = current.dew_point,
                    uvi = current.uvi.roundToInt().toString(),
                    clouds = current.clouds,
                    visibility = current.visibility,
                    wind = "${((current.wind_speed * 3.6 * 100) / 100).roundToInt()} km/h ${toHumanFromDegrees(current.wind_deg)}",
                    //rain = "${current.weather.first().rain.roundToInt()} mm/24h",
                    backgroundImage = backgroundImageFromWeather(current.weather.first().main)
                )

                emit(currentWeather)

            }
        }

    //TODO: usunąc stąd te 3 metody poniżej
    private fun backgroundImageFromWeather(input: String): Int {
        return when (weatherCondition(input)) {
            WeatherCondition.Thunderstorm -> R.drawable.new_thunder
            WeatherCondition.Drizzle -> R.drawable.new_rain
            WeatherCondition.Rain -> R.drawable.new_rain
            WeatherCondition.Snow -> R.drawable.new_snow
            WeatherCondition.Fog -> R.drawable.new_fog
            WeatherCondition.Clear -> R.drawable.clear_new
            WeatherCondition.Clouds -> R.drawable.clouds_new
            WeatherCondition.Unknown -> R.drawable.unknown
        }
    }

    private fun weatherCondition(input: String) = when (input) {
        "Thunderstorm" -> WeatherCondition.Thunderstorm
        "Drizzle" -> WeatherCondition.Drizzle
        "Rain" -> WeatherCondition.Rain
        "Snow" -> WeatherCondition.Snow
        "Mist", "Fog", "Smoke", "Haze", "Dust", "Sand", "Ash", "Squall", "Tornado" -> WeatherCondition.Fog
        "Clear" -> WeatherCondition.Clear
        "Clouds" -> WeatherCondition.Clouds
        else -> WeatherCondition.Unknown
    }

    enum class WeatherCondition {
        Thunderstorm, Drizzle, Rain, Snow, Fog, Clear, Clouds, Unknown
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

/*suspend fun getDailyForecastWeather(): Flow<List<WeatherDailyResponse>> {
    val weather = weatherServiceApi.getWeather()

    val body = weather.body() ?: throw Exception("No data")

    return flowOf(body.daily.takeLast(7))
}*/

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