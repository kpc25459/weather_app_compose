package net.dev.weather.data

import kotlinx.datetime.LocalDateTime

data class WeatherHourlyC(
    val dt: LocalDateTime,
    val temp: Double,
    val weatherIcon: String
)