package net.dev.weather.data

import kotlinx.datetime.LocalDateTime

data class AirPollutionForecast(
    val dt: LocalDateTime,
    val aqi: Int,
    val no2: Double,
    val o3: Double,
    val pm2_5: Double,
    val pm10: Double,
)