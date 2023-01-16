package net.dev.weather.data

import kotlinx.datetime.LocalDateTime
import net.dev.weather.api.WeatherResponse

data class WeatherHourly(
    val dt: LocalDateTime,
    val sunrise: Int,
    val sunset: Int,
    val temp: Double,
    val feels_like: Double,
    val pressure: Int,
    val humidity: Int,
    val dew_point: Double,
    val uvi: Double,
    val clouds: Int,
    val visibility: Int,
    val wind_speed: Double,
    val wind_deg: Int,
    val wind_gust: Double,
    //TODO: tutaj inny typ
    val weather: List<WeatherResponse>,
    val pop: Double,
    val rain: Double
)