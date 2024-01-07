package net.dev.weather.network.api

import net.dev.weather.BuildConfig
import net.dev.weather.network.model.AirPollutionResponse
import net.dev.weather.network.model.CurrentLocation
import net.dev.weather.network.model.OneCallResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherServiceApi {

    @GET("/geo/1.0/reverse")
    suspend fun getReverseLocation(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("limit") limit: Int = 1,
        @Query("appid") appid: String = BuildConfig.OPEN_WEATHER_API_KEY,
    ): Response<List<CurrentLocation>>

    @GET("/data/2.5/onecall")
    suspend fun getWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("exclude") exclude: String = "alerts",
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "pl",
        @Query("appid") appid: String = BuildConfig.OPEN_WEATHER_API_KEY,
    ): Response<OneCallResponse>

    @GET("/data/2.5/air_pollution")
    suspend fun getAirPollution(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") appid: String = BuildConfig.OPEN_WEATHER_API_KEY,
    ): Response<AirPollutionResponse>

    @GET("/data/2.5/air_pollution/forecast")
    suspend fun getAirPollutionForecast(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") appid: String = BuildConfig.OPEN_WEATHER_API_KEY,
    ): Response<AirPollutionResponse>

    companion object {
        const val BASE_URL = "http://api.openweathermap.org/"
    }
}