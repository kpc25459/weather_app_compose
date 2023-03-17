package net.dev.weather.ui.model

data class UiAirPollutionForecast(
    val dt: String,
    val aqi: Int,
    val co: Double,
    val no: Double,
    val no2: String,
    val o3: String,
    val so2: Double,
    val pm2_5: Double,
    val pm2_5String: String,
    val pm10: Double,
    val pm10String: String,
    val nh3: Double,
)
