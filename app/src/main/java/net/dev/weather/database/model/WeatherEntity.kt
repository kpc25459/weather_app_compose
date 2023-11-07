package net.dev.weather.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "weather"
)
data class WeatherEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val dt: Long,
    val sunrise: Long,
    val sunset: Long,
    val temp: Double,
    val feels_like: Double,
    val pressure: Int,
    val humidity: Int,
    val uvi: Double,
    val wind: Double,
    val windDirection: String,
    val rain: Double,
    val weatherCondition: String
)