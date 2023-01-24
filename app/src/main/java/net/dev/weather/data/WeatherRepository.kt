package net.dev.weather.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import net.dev.weather.R
import net.dev.weather.api.WeatherServiceApi
import net.dev.weather.fromAqiIndex
import net.dev.weather.toHumanFromDegrees
import kotlin.math.roundToInt

interface WeatherRepository {
    val location: Flow<String>
    val weatherCurrent: Flow<WeatherCurrent>
    val weatherDaily: Flow<List<WeatherDaily>>
    val weatherHourly: Flow<List<WeatherHourly>>

    val airPollution: Flow<List<AirPollutionForecast>>

    val airQuality: Flow<Int>
}

//TODO: tutaj tylko zwracać dane z cache lub z api
//TODO: mapować na warstwie aplikacji
class NetworkRepository(private val weatherServiceApi: WeatherServiceApi) : WeatherRepository {

    override val location: Flow<String>
        get() = flow {
            val reverseLocationResponse = weatherServiceApi.getReverseLocation()
            val location = reverseLocationResponse.body()?.first()?.name ?: "Unknown"
            emit(location)
        }

    override val weatherCurrent: Flow<WeatherCurrent>
        get() = flow {
            val weather = weatherServiceApi.getWeather()
            if (weather.isSuccessful) {
                val body = weather.body()!!
                val current = body.current

                val timeZone = TimeZone.of(body.timezone)

                val dailyFirst = body.daily.first()

                val currentWeather = WeatherCurrent(
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
                    rain = "${dailyFirst.rain.roundToInt()} mm/24h",
                    backgroundImage = backgroundImageFromWeather(current.weather.first().main)
                )
                emit(currentWeather)
            }
        }

    //TODO: usunąc stąd te 3 metody poniżej - na warstwę aplikacji
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

    override val weatherDaily: Flow<List<WeatherDaily>>
        get() = flow {
            val weather = weatherServiceApi.getWeather()
            if (weather.isSuccessful) {

                val body = weather.body()!!

                val timeZone = TimeZone.of(body.timezone)

                val last7days = body.daily.take(7)

                emit(last7days.map {
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

    override val weatherHourly: Flow<List<WeatherHourly>>
        get() = flow {
            val weather = weatherServiceApi.getWeather()

            if (weather.isSuccessful) {

                val body = weather.body()!!
                val timeZone = TimeZone.of(body.timezone)

                val last24h = body.hourly.take(24)
                emit(last24h.map {
                    WeatherHourly(
                        dt = Instant.fromEpochSeconds(it.dt.toLong()).toLocalDateTime(timeZone),
                        sunrise = it.sunrise,
                        sunset = it.sunset,
                        temp = it.temp,
                        feels_like = it.feels_like,
                        pressure = it.pressure,
                        humidity = it.humidity,
                        dew_point = it.dew_point,
                        uvi = it.uvi,
                        clouds = it.clouds,
                        visibility = it.visibility,
                        wind_speed = it.wind_speed,
                        wind_deg = it.wind_deg,
                        wind_gust = it.wind_gust,
                        weather = it.weather,
                        pop = it.pop,
                        rain = it.rain?.`1h` ?: 0.0
                    )
                })
            }
        }

    override val airPollution: Flow<List<AirPollutionForecast>>
        get() = flow {
            val airPollution = weatherServiceApi.getAirPollutionForecast()

            if (airPollution.isSuccessful) {
                val body = airPollution.body()!!
                //TODO: zahardkodowana strefa czasowa - do poprawy
                val timeZone = TimeZone.of("Europe/Warsaw")

                emit(body.list.map {
                    AirPollutionForecast(
                        dt = Instant.fromEpochSeconds(it.dt.toLong()).toLocalDateTime(timeZone),
                        aqi = fromAqiIndex(it.main.aqi),
                        co = it.components.co,
                        no = it.components.no,
                        no2 = it.components.no2,
                        o3 = it.components.o3,
                        so2 = it.components.so2,
                        pm2_5 = it.components.pm2_5,
                        pm10 = it.components.pm10,
                        nh3 = it.components.nh3
                    )
                })
            }
        }

    override val airQuality: Flow<Int>
        get() = flow {
            val airQuality = weatherServiceApi.getAirPollution()
            if (airQuality.isSuccessful) {
                val body = airQuality.body()!!
                emit(body.list.first().main.aqi)
            }
        }
}
