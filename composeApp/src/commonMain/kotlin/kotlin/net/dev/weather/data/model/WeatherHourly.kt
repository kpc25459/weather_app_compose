package kotlin.net.dev.weather.data.model

import kotlinx.datetime.LocalDateTime

data class WeatherHourly(
    val dt: LocalDateTime,
    val temp: Double,
    val weatherIcon: String
)