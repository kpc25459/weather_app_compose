package kotlin.net.dev.weather.data.model

import kotlin.net.dev.weather.data.model.WeatherDaily

data class WeatherForecast(
    val daily: List<WeatherDaily>
)