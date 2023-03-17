package net.dev.weather.data

data class WeatherC(
    val current: WeatherCurrentC,
    val daily: List<WeatherDailyC>,
    val hourly: List<WeatherHourlyC>
)