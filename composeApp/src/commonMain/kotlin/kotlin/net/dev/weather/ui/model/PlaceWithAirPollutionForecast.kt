package kotlin.net.dev.weather.ui.model

import kotlin.net.dev.weather.data.model.AirPollutionForecast
import kotlin.net.dev.weather.data.model.Place

data class PlaceWithAirPollutionForecast(
    val place: Place,
    val airPollutionForecast: List<AirPollutionForecast>
)