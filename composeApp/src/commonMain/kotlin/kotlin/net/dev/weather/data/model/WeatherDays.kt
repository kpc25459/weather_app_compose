package net.dev.weather.data.model

data class WeatherDays(
    val current: WeatherCurrent,
    val daily: List<WeatherDaily>,
    val hourly: List<WeatherHourly>
)