package net.dev.weather.network.api

import kotlinx.serialization.Serializable

@Serializable
data class RainHourly(
    val `1h`: Double
)