package net.dev.weather.data

import kotlinx.datetime.LocalDateTime

data class CurrentWeather(
    val dt: LocalDateTime,
    val sunrise: String,
    val sunset: String,
    val temp: Int,
    val feels_like: String,
    val pressure: String,
    val humidity: String,
    val dew_point: Double,
    val uvi: String,
    val clouds: Int,
    val visibility: Int,
    val wind: String,
    val rain: String,
    val backgroundImage: Int
)