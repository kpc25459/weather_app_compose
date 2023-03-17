package net.dev.weather.ui.model

import kotlinx.datetime.LocalDateTime

data class UiWeatherHourly(
    val dt: LocalDateTime,
    val temp: Double,
    val weatherIcon: String
)
