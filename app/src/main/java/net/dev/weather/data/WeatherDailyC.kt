package net.dev.weather.data

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime

data class WeatherDailyC(
    val dt: LocalDateTime,
    val description: String,
    val sunrise: LocalTime,
    val sunset: LocalTime,
    val tempDay: Double,
    val tempNight: Double,
    val pressure: Int,
    val humidity: Int,
    /**
     * Wind km/h
     */
    val wind: Double,
    /**
     * Rain mm / 24h
     */
    val rain: Double,
    val uvi: Double,
    val icon: String,
    val windDirection: String
)