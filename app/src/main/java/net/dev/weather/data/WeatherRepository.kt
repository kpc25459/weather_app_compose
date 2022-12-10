package net.dev.weather.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import net.dev.weather.api.OneCallResponse
import net.dev.weather.api.WeatherDailyResponse
import net.dev.weather.api.WeatherHourlyResponse
import net.dev.weather.api.WeatherServiceApi

class WeatherRepository(private val weatherServiceApi: WeatherServiceApi) {

    //TODO: tutaj pozbyć się wykrzykników
    suspend fun getLocation(): Flow<String> {
        val reverseLocationResponse = weatherServiceApi.getReverseLocation()
        val location = reverseLocationResponse.body()?.first()?.name

        return flowOf(location!!)
    }

    suspend fun getWeather(): Flow<OneCallResponse> {
        val weather = weatherServiceApi.getWeather()

        val body = weather.body() ?: throw Exception("No data")

        return flowOf(body)
    }


    suspend fun getCurrentWeather(): Flow<WeatherHourlyResponse> {
        val weather = weatherServiceApi.getWeather()

        val body = weather.body() ?: throw Exception("No data")

        return flowOf(body.current)
    }

    suspend fun getDailyForecastWeather(): Flow<List<WeatherDailyResponse>> {
        val weather = weatherServiceApi.getWeather()

        val body = weather.body() ?: throw Exception("No data")

        return flowOf(body.daily.takeLast(7))
    }

    suspend fun getHourlyForecastWeather(): Flow<List<WeatherHourlyResponse>> {
        val weather = weatherServiceApi.getWeather()

        val body = weather.body() ?: throw Exception("No data")

        return flowOf(body.hourly.take(24))
    }

    suspend fun getAirQuality(): Flow<Int> {

        val airQuality = weatherServiceApi.getAirPollution()

        val body = airQuality.body() ?: throw Exception("No data")

        val aqi = body.list.first().main.aqi

        return flowOf(aqi)
    }
}