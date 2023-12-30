package kotlin.net.dev.weather.network.model

data class WeatherResponse(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)