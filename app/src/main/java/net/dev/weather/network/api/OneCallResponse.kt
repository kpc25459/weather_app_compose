package net.dev.weather.network.api


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OneCallResponse(
    @SerialName("current")
    val current: Current,
    @SerialName("daily")
    val daily: List<Daily>,
    @SerialName("hourly")
    val hourly: List<Hourly>,
    @SerialName("lat")
    val lat: Double,
    @SerialName("lon")
    val lon: Double,
    @SerialName("minutely")
    val minutely: List<Minutely>,
    @SerialName("timezone")
    val timezone: String,
    @SerialName("timezone_offset")
    val timezoneOffset: Int
)