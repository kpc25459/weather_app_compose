package kotlin.net.dev.weather.data.model

data class Weather(
    val current: WeatherCurrent,
    val daily: List<WeatherDaily>,
    val hourly: List<WeatherHourly>,
    val aqi: Int,
)