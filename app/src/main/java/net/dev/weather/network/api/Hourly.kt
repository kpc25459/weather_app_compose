package net.dev.weather.network.api


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Hourly(
    val dt: Int,
    val temp: Double,
    @SerialName("feels_like")
    val feelsLike: Double,
    val pressure: Int,
    val humidity: Int,
    @SerialName("dew_point")
    val dewPoint: Double,
    @SerialName("uvi")
    val uvi: Double,
    @SerialName("clouds")
    val clouds: Int,
    @SerialName("visibility")
    val visibility: Int? = null,
    @SerialName("wind_speed")
    val windSpeed: Double,
    @SerialName("wind_deg")
    val windDeg: Int,
    @SerialName("wind_gust")
    val windGust: Double,

    @SerialName("pop")
    val pop: Double,


    @SerialName("weather")
    val weather: List<Weather>,

    @SerialName("rain")
    val rain: RainHourly? = null,

    @SerialName("snow")
    val snow: SnowHourly? = null
)