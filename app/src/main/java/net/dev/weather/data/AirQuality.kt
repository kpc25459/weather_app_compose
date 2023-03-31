package net.dev.weather.data

import net.dev.weather.ui.model.UiAirPollutionForecast

data class AirQuality(
    val location: String,
    val airPollutionForecast: List<UiAirPollutionForecast> = emptyList()
)