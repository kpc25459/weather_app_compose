package net.dev.weather.data.repository

import kotlinx.coroutines.flow.Flow
import net.dev.weather.data.model.AirPollutionForecast
import net.dev.weather.data.model.Weather

interface WeatherRepository {
    val weather: Flow<Weather>

    val airPollutionForecast: Flow<List<AirPollutionForecast>>

    val airPollutionCurrent: Flow<Int>
}