package net.dev.weather.data

import kotlinx.datetime.LocalDateTime

data class AirPollutionForecast(
    val dt: LocalDateTime,
    val aqi: Int,
    //val airQuality: String,
    val co: Double,
    val no: Double,
    val no2: Double,
    val o3: Double,
    val so2: Double,
    val pm2_5: Double,
    val pm10: Double,
    val nh3: Double,
)
