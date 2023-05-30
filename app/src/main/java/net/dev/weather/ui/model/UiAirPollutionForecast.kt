package net.dev.weather.ui.model

import kotlinx.serialization.Serializable

@Serializable
data class UiAirPollutionForecast(
    val dt: String,
    val aqi: Int,
    val no2: String,
    val o3: String,
    val pm2_5: Double,
    val pm2_5String: String,
    val pm10: Double,
    val pm10String: String,
)
