package kotlin.net.dev.weather.ui.model

import kotlin.net.dev.weather.data.model.Weather

data class PlaceWithCurrentWeather(
    val place: String,
    val weather: Weather,
)