package kotlin.net.dev.weather.network.model

data class FeelsLikeResponse(
    val day: Double,
    val night: Double,
    val eve: Double,
    val morn: Double
)