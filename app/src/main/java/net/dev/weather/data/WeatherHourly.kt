package net.dev.weather.data

import kotlinx.datetime.LocalDateTime

data class WeatherHourly(
    val dt: LocalDateTime,
    val temp: Double,
    val weatherIcon: String
)
