package kotlin.net.dev.weather.data.repository

import kotlinx.coroutines.flow.Flow
import kotlin.net.dev.weather.data.model.AirPollutionForecast
import kotlin.net.dev.weather.data.model.Weather

interface WeatherRepository {
    //val weather: Flow<WeatherDays>

    suspend fun weatherFor(latitude: Double, longitude: Double): Weather

    val airPollutionForecast: Flow<List<AirPollutionForecast>>

    val airPollutionCurrent: Flow<Int>
}