package net.dev.weather.network.model

import kotlinx.serialization.Serializable

@Serializable
data class WeatherDailyResponse(
    val dt: Int,
    val sunrise: Int,
    val sunset: Int,
    val moonrise: Int,
    val moonset: Int,
    val moon_phase: Double,
    val temp: TempResponse,
    val feels_like: FeelsLikeResponse,
    val pressure: Int,
    val humidity: Int,
    val dew_point: Double,
    val wind_speed: Double,
    val wind_deg: Int,
    val wind_gust: Double,
    val weather: List<WeatherResponse>,
    val clouds: Int,
    val pop: Double,
    val rain: Double,
    val uvi: Double
)