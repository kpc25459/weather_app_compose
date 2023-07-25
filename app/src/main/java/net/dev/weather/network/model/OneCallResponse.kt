package net.dev.weather.network.model

data class OneCallResponse(
    val lat: Double,
    val lon: Double,
    val timezone: String,
    val timezone_offset: Int,

    val current: WeatherHourlyResponse,
    val hourly: List<WeatherHourlyResponse>,
    val daily: List<WeatherDailyResponse>
)