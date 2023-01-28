package net.dev.weather.data

import kotlinx.datetime.LocalDateTime

data class WeatherCurrent(
    val dt: LocalDateTime,
    val sunrise: String,
    val sunset: String,
    val temp: Int,
    val feels_like: String,
    val pressure: String,
    val humidity: String,
    val uvi: String,
    val wind: String,
    val rain: String,
    val backgroundImage: Int
)
