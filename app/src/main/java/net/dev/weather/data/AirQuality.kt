package net.dev.weather.data

import kotlinx.serialization.Serializable
import net.dev.weather.ui.model.UiAirPollutionForecast

@Serializable
data class AirQuality(
    val place: Place,
    val airPollutionForecast: List<UiAirPollutionForecast> = emptyList()
)