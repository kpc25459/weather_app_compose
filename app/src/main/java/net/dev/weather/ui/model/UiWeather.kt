package net.dev.weather.ui.model

data class UiWeather(
    val current: UiWeatherCurrent,
    val daily: List<UiWeatherDaily>,
    val hourly: List<UiWeatherHourly>
)
