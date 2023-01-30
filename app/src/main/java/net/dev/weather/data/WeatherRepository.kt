package net.dev.weather.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import net.dev.weather.api.WeatherServiceApi
import net.dev.weather.backgroundImageFromWeather
import net.dev.weather.fromAqiIndex
import net.dev.weather.toHumanFromDegrees
import kotlin.math.roundToInt

interface WeatherRepository {
    val location: Flow<String>
    val weatherCurrent: Flow<WeatherCurrent>
    val weatherDaily: Flow<List<WeatherDaily>>
    val weatherHourly: Flow<List<WeatherHourly>>

    val airPollutionForecast: Flow<List<AirPollutionForecast>>

    val airQuality: Flow<Int>
}

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

                val currentWeather = WeatherCurrent(
                    dt = Instant.fromEpochSeconds(current.dt.toLong()).toLocalDateTime(timeZone),
                    sunrise = Instant.fromEpochSeconds(current.sunrise.toLong()).toLocalDateTime(timeZone).time.toString().substringBeforeLast(":"),
                    sunset = Instant.fromEpochSeconds(current.sunset.toLong()).toLocalDateTime(timeZone).time.toString().substringBeforeLast(":"),
                    temp = current.temp.roundToInt(),
                    feels_like = "${current.feels_like.roundToInt()} °",
                    pressure = "${current.pressure} hPa",
                    humidity = "${current.humidity} %",
                    uvi = current.uvi.roundToInt().toString(),
                    wind = "${((current.wind_speed * 3.6 * 100) / 100).roundToInt()} km/h ${toHumanFromDegrees(current.wind_deg)}",
                    rain = "${body.daily.first().rain.roundToInt()} mm/24h",
                    backgroundImage = backgroundImageFromWeather(current.weather.first().main)
                )
                emit(currentWeather)
            }
        }

    override val weatherDaily: Flow<List<WeatherDaily>>
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

    override val weatherHourly: Flow<List<WeatherHourly>>
        get() = flow {
            val weather = weatherServiceApi.getWeather()

            if (weather.isSuccessful) {

                val body = weather.body()!!
                val timeZone = TimeZone.of(body.timezone)

                val last24h = body.hourly
                emit(last24h.map {
                    WeatherHourly(
                        dt = Instant.fromEpochSeconds(it.dt.toLong()).toLocalDateTime(timeZone),
                        temp = it.temp,
                        weatherIcon = it.weather[0].icon
                    )
                })
            }
        }

    override val airPollutionForecast: Flow<List<AirPollutionForecast>>
        get() = flow {
            val airPollution = weatherServiceApi.getAirPollutionForecast()

            if (airPollution.isSuccessful) {
                val body = airPollution.body()!!
                //TODO: zahardkodowana strefa czasowa - do poprawy
                val timeZone = TimeZone.of("Europe/Warsaw")

                emit(body.list.map {
                    AirPollutionForecast(
                        dt = Instant.fromEpochSeconds(it.dt.toLong()).toLocalDateTime(timeZone),
                        aqi = it.main.aqi,
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
