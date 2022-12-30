package net.dev.weather.data

import kotlinx.datetime.LocalDateTime

data class WeatherDaily(
    val dt: LocalDateTime,
    val description: String,
    val sunrise: String,
    val sunset: String,
    val temp: String,
    val pressure: String,
    val humidity: String,
    val wind: String,
    val rain: String,
    val uvi: String
)