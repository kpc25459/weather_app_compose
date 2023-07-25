package net.dev.weather.ui.model

import net.dev.weather.data.model.Weather

data class PlaceWithCurrentWeather(
    val place: String,
    val weather: Weather,
    val airPollutionCurrent: Int,
)