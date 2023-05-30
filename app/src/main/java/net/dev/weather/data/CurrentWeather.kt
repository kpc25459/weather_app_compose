package net.dev.weather.data

import kotlinx.serialization.Serializable
import net.dev.weather.ui.model.UiWeatherCurrent
import net.dev.weather.ui.model.UiWeatherDaily
import net.dev.weather.ui.model.UiWeatherHourly

@Serializable
data class CurrentWeather(
    val location: String,
    val current: UiWeatherCurrent,
    val daily: List<UiWeatherDaily>,
    val hourlyForecast: List<UiWeatherHourly>,
    val airPollutionCurrent: Int = 0,
)