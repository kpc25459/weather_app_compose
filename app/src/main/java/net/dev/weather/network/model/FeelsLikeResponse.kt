package net.dev.weather.network.model

import kotlinx.serialization.Serializable

@Serializable
data class FeelsLikeResponse(
    val day: Double,
    val night: Double,
    val eve: Double,
    val morn: Double
)