package net.dev.weather.data

import kotlinx.datetime.LocalDateTime

data class WeatherDaily(
    val dt: LocalDateTime,
    val sunrise: Int,
    val sunset: Int,
    val moonrise: Int,
    val moonset: Int,
    val moon_phase: Double,
    val temp: Double,
    val feels_like: Double,
    val pressure: Int,
    val humidity: Int,
    val dew_point: Double,
    val wind_speed: Double,
    val wind_deg: Int,
    val wind_gust: Double,
    //val weather: List<Weather>,
    val clouds: Int,
    val pop: Double,
    val rain: Double,
    val uvi: Double
)

/*
data class Weather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)
*/
