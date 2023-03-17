package net.dev.weather.ui.model

import kotlinx.datetime.LocalDateTime

data class UiWeatherCurrent(
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
