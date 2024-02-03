package net.dev.weather.network.model

import kotlinx.serialization.Serializable

@Serializable
data class WeatherResponse(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)