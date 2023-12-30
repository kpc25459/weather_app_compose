package net.dev.weather.ui.model

import net.dev.weather.data.model.AirPollutionForecast
import net.dev.weather.data.model.Place

data class PlaceWithAirPollutionForecast(
    val place: Place,
    val airPollutionForecast: List<AirPollutionForecast>
)