package net.dev.weather.api

data class WeatherResponse(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)