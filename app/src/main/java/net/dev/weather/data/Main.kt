package net.dev.weather.data

import net.dev.weather.ui.model.UiAirPollutionForecast
import net.dev.weather.ui.model.UiWeatherCurrent
import net.dev.weather.ui.model.UiWeatherDaily
import net.dev.weather.ui.model.UiWeatherHourly

data class Main(
    val location: String,
    val current: UiWeatherCurrent,
    val daily: List<UiWeatherDaily>,
    val hourlyForecast: List<UiWeatherHourly>,
    val airPollutionCurrent: Int = 0,
    val airPollutionForecast: List<UiAirPollutionForecast> = emptyList()
)