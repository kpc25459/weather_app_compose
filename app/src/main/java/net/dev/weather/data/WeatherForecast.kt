package net.dev.weather.data

import net.dev.weather.ui.model.UiWeatherDaily

data class WeatherForecast(
    val daily: List<UiWeatherDaily>
)