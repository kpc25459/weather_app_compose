package net.dev.weather.api

data class FeelsLikeResponse(
    val day: Double,
    val night: Double,
    val eve: Double,
    val morn: Double
)