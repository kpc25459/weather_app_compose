package net.dev.weather.ui.model

import kotlinx.datetime.LocalDateTime

data class UiWeatherDaily(
    val dt: LocalDateTime,
    val description: String,
    val sunrise: String,
    val sunset: String,
    val temp: String,
    val pressure: String,
    val humidity: String,
    val wind: String,
    val rain: String,
    val uvi: String,
    val icon: String
)