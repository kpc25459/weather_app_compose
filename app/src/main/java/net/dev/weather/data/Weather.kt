package net.dev.weather.data

data class Weather(
    val current: WeatherCurrent,
    val daily: List<WeatherDaily>,
    val hourly: List<WeatherHourly>
)
