package net.dev.weather.data

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import net.dev.weather.api.WeatherServiceApi
import net.dev.weather.backgroundImageFromWeather
import net.dev.weather.toHumanFromDegrees
import kotlin.math.roundToInt

interface WeatherRepository {

    val location: Flow<String>

    val weather: Flow<Weather>

    val airPollutionForecast: Flow<List<AirPollutionForecast>>

    val airQuality: Flow<Int>
}

class NetworkRepository(private val weatherServiceApi: WeatherServiceApi, private val refreshIntervalMs: Long = 1000 * 60) : WeatherRepository {

    override val location: Flow<String>
        get() = flow {
            val reverseLocationResponse = weatherServiceApi.getReverseLocation()
            val location = reverseLocationResponse.body()?.first()?.name ?: "Unknown"
            emit(location)
        }

    override val weather: Flow<Weather>
        get() = flow {
            while (true) {
                val weatherResponse = weatherServiceApi.getWeather()
                if (weatherResponse.isSuccessful) {
                    val body = weatherResponse.body()!!

                    val timeZone = TimeZone.of(body.timezone)

                    val current1 = body.current

                    val current = WeatherCurrent(
                        dt = Instant.fromEpochSeconds(current1.dt.toLong()).toLocalDateTime(timeZone),
                        sunrise = Instant.fromEpochSeconds(current1.sunrise.toLong()).toLocalDateTime(timeZone).time.toString().substringBeforeLast(":"),
                        sunset = Instant.fromEpochSeconds(current1.sunset.toLong()).toLocalDateTime(timeZone).time.toString().substringBeforeLast(":"),
                        temp = current1.temp.roundToInt(),
                        feels_like = "${current1.feels_like.roundToInt()} °",
                        pressure = "${current1.pressure} hPa",
                        humidity = "${current1.humidity} %",
                        uvi = current1.uvi.roundToInt().toString(),
                        wind = "${((current1.wind_speed * 3.6 * 100) / 100).roundToInt()} km/h ${toHumanFromDegrees(current1.wind_deg)}",
                        rain = "${body.daily.first().rain.roundToInt()} mm/24h",
                        backgroundImage = backgroundImageFromWeather(current1.weather.first().main)
                    )

                    val weatherDaily = body.daily.map {
                        WeatherDaily(
                            Instant.fromEpochSeconds(it.dt.toLong()).toLocalDateTime(timeZone),
                            description = it.weather.first().description,
                            sunrise = Instant.fromEpochSeconds(it.sunrise.toLong()).toLocalDateTime(timeZone).time.toString().substringBeforeLast(":"),
                            sunset = Instant.fromEpochSeconds(it.sunset.toLong()).toLocalDateTime(timeZone).time.toString().substringBeforeLast(":"),
                            temp = "${it.temp.day.roundToInt()}°C / ${it.temp.night.roundToInt()}°C",
                            pressure = "${it.pressure} hPa",
                            humidity = "${it.humidity} %",
                            wind = "${((it.wind_speed * 3.6 * 100) / 100).roundToInt()} km/h ${toHumanFromDegrees(it.wind_deg)}",
                            rain = "${it.rain.roundToInt()} mm/24h",
                            uvi = it.uvi.roundToInt().toString(),
                            icon = it.weather[0].icon
                        )
                    }

                    val weatherHourly = body.hourly.map {
                        WeatherHourly(
                            dt = Instant.fromEpochSeconds(it.dt.toLong()).toLocalDateTime(timeZone),
                            temp = it.temp,
                            weatherIcon = it.weather[0].icon
                        )
                    }

                    emit(
                        Weather(
                            current = current,
                            daily = weatherDaily,
                            hourly = weatherHourly
                        )
                    )
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
                delay(refreshIntervalMs)
            }
        }

    override val airQuality: Flow<Int>
        get() = flow {
            while (true) {
                val airQuality = weatherServiceApi.getAirPollution()
                if (airQuality.isSuccessful) {
                    val body = airQuality.body()!!
                    emit(body.list.first().main.aqi)
                }
                delay(refreshIntervalMs)
            }
        }

}
