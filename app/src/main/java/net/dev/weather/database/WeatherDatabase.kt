package net.dev.weather.database

import androidx.room.Database
import androidx.room.RoomDatabase
import net.dev.weather.database.dao.WeatherDao
import net.dev.weather.database.model.WeatherEntity

@Database(
    entities = [
        WeatherEntity::class,
    ],
    version = 1,
    exportSchema = false,
)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
}