package net.dev.weather.database.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import net.dev.weather.database.model.WeatherEntity

@Dao
interface WeatherDao {

    @Query(
        value = """        
            SELECT * FROM weather
            ORDER BY dt ASC""",
    )
    fun getAll(): Flow<List<WeatherEntity>>

    @Query(
        value = """
            SELECT * FROM weather
            ORDER BY dt DESC
            LIMIT 1
        """,
    )
    fun getCurrentWeather(): Flow<WeatherEntity>


}