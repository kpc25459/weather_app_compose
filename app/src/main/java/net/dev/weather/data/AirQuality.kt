package net.dev.weather.data

import net.dev.weather.ui.model.UiAirPollutionForecast

data class AirQuality(
    val place: Place,
    val airPollutionForecast: List<UiAirPollutionForecast> = emptyList()
)