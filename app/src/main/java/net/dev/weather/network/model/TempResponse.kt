package net.dev.weather.network.model

import kotlinx.serialization.Serializable

@Serializable
data class TempResponse(
    val day: Double,
    val min: Double,
    val max: Double,
    val night: Double,
    val eve: Double,
    val morn: Double
)