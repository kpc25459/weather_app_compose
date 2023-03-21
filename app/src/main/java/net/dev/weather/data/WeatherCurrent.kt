package net.dev.weather.data

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime

data class WeatherCurrent(
    val dt: LocalDateTime,
    val sunrise: LocalTime,
    val sunset: LocalTime,
    val temp: Double,
    val feels_like: Double,
    val pressure: Int,
    val humidity: Int,
    val uvi: Double,
    /**
     * Wind km/h
     */
    val wind: Double,
    val windDirection: String,
    /**
     * Rain mm / 24h
     */
    val rain: Double,
    val backgroundImage: Int
)