package net.dev.weather.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherServiceApi {

    @GET("/geo/1.0/reverse")
    suspend fun getReverseLocation(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("limit") limit: Int = 1,
        @Query("appid") appid: String = WeatherServiceApi.appid,
    ): Response<List<CurrentLocation>>

    @GET("/data/2.5/onecall")
    suspend fun getWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("exclude") exclude: String = "alerts",
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "pl",
        @Query("appid") appid: String = WeatherServiceApi.appid,
    ): Response<OneCallResponse>

    @GET("/data/2.5/air_pollution")
    suspend fun getAirPollution(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") appid: String = WeatherServiceApi.appid,
    ): Response<AirPollutionResponse>

    @GET("/data/2.5/air_pollution/forecast")
    suspend fun getAirPollutionForecast(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") appid: String = WeatherServiceApi.appid,
    ): Response<AirPollutionResponse>

    companion object {
        const val BASE_URL = "http://api.openweathermap.org/"
        private const val appid = "8c27af4c68a4a3cc4c9d8d009ece6201"
    }
}


data class AirPollutionResponse(val list: List<AirPollution>)

data class AirPollution(val main: AirPollutionMain, val components: AirPollutionComponents, val dt: Int)

data class AirPollutionMain(val aqi: Int)

data class AirPollutionComponents(val co: Double, val no: Double, val no2: Double, val o3: Double, val so2: Double, val pm2_5: Double, val pm10: Double, val nh3: Double)

data class FeelsLikeResponse(
    val day: Double,
    val night: Double,
    val eve: Double,
    val morn: Double
)

data class OneCallResponse(
    val lat: Double,
    val lon: Double,
    val timezone: String,
    val timezone_offset: Int,

    val current: WeatherHourlyResponse,
    val hourly: List<WeatherHourlyResponse>,
    val daily: List<WeatherDailyResponse>
)

data class TempResponse(
    val day: Double,
    val min: Double,
    val max: Double,
    val night: Double,
    val eve: Double,
    val morn: Double
)


data class WeatherResponse(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

data class WeatherDailyResponse(
    val dt: Int,
    val sunrise: Int,
    val sunset: Int,
    val moonrise: Int,
    val moonset: Int,
    val moon_phase: Double,
    val temp: TempResponse,
    val feels_like: FeelsLikeResponse,
    val pressure: Int,
    val humidity: Int,
    val dew_point: Double,
    val wind_speed: Double,
    val wind_deg: Int,
    val wind_gust: Double,
    val weather: List<WeatherResponse>,
    val clouds: Int,
    val pop: Double,
    val rain: Double,
    val uvi: Double
)

data class WeatherHourlyResponse(
    val dt: Int,
    val sunrise: Int,
    val sunset: Int,
    val temp: Double,
    val feels_like: Double,
    val pressure: Int,
    val humidity: Int,
    val dew_point: Double,
    val uvi: Double,
    val clouds: Int,
    val visibility: Int,
    val wind_speed: Double,
    val wind_deg: Int,
    val wind_gust: Double,
    val weather: List<WeatherResponse>,
    val pop: Double,
    val rain: RainHourly?
)

data class RainHourly(
    val `1h`: Double
)

data class CurrentLocation(
    val name: String
)