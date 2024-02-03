package net.dev.weather.network.api

import kotlinx.serialization.Serializable

@Serializable
data class SnowHourly(
    val `1h`: Double
)