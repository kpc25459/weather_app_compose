package kotlin.net.dev.weather.network.repository

import kotlinx.datetime.Instant
import kotlinx.datetime.toLocalDateTime
import net.dev.weather.utils.defaultTimeZone
import net.dev.weather.utils.toHumanFromDegrees
import kotlin.net.dev.weather.data.model.AirPollutionForecast
import kotlin.net.dev.weather.data.model.WeatherCurrent
import kotlin.net.dev.weather.data.model.WeatherDaily
import kotlin.net.dev.weather.data.model.WeatherDays
import kotlin.net.dev.weather.data.model.WeatherHourly
import kotlin.net.dev.weather.network.model.AirPollutionResponse
import kotlin.net.dev.weather.network.model.OneCallResponse
import kotlin.net.dev.weather.network.model.WeatherHourlyResponse

fun AirPollutionResponse.mapToAirPollutionForecast(): List<AirPollutionForecast> {
    return this.list.map {
        AirPollutionForecast(
            dt = Instant.fromEpochSeconds(it.dt.toLong()).toLocalDateTime(defaultTimeZone),
            aqi = it.main.aqi,
            no2 = it.components.no2,
            o3 = it.components.o3,
            pm2_5 = it.components.pm2_5,
            pm10 = it.components.pm10,
        )
    }
}

fun OneCallResponse.mapToWeatherDays(): WeatherDays {
    val current1: WeatherHourlyResponse = this.current

    val current = WeatherCurrent(
        dt = Instant.fromEpochSeconds(current1.dt.toLong()).toLocalDateTime(defaultTimeZone),
        sunrise = Instant.fromEpochSeconds(current1.sunrise.toLong()).toLocalDateTime(defaultTimeZone).time,
        sunset = Instant.fromEpochSeconds(current1.sunset.toLong()).toLocalDateTime(defaultTimeZone).time,
        temp = current1.temp,
        feels_like = current1.feels_like,
        pressure = current1.pressure,
        humidity = current1.humidity,
        uvi = current1.uvi,
        wind = (current1.wind_speed * 3.6 * 100) / 100,
        windDirection = toHumanFromDegrees(current1.wind_deg),
        rain = this.daily.first().rain,
        weatherCondition = current1.weather.first().main,
    )

    val weatherDaily = this.daily.map {
        WeatherDaily(
            dt = Instant.fromEpochSeconds(it.dt.toLong()).toLocalDateTime(defaultTimeZone),
            description = it.weather.first().description,
            sunrise = Instant.fromEpochSeconds(it.sunrise.toLong()).toLocalDateTime(defaultTimeZone).time,
            sunset = Instant.fromEpochSeconds(it.sunset.toLong()).toLocalDateTime(defaultTimeZone).time,
            tempDay = it.temp.day,
            tempNight = it.temp.night,
            pressure = it.pressure,
            humidity = it.humidity,
            wind = (it.wind_speed * 3.6 * 100) / 100,
            windDirection = toHumanFromDegrees(it.wind_deg),
            rain = it.rain,
            uvi = it.uvi,
            icon = it.weather[0].icon
        )
    }

    val weatherHourly = this.hourly.map {
        WeatherHourly(
            dt = Instant.fromEpochSeconds(it.dt.toLong()).toLocalDateTime(defaultTimeZone),
            temp = it.temp,
            weatherIcon = it.weather[0].icon
        )
    }

    return WeatherDays(
        current = current,
        daily = weatherDaily,
        hourly = weatherHourly
    )
}