package net.dev.weather.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

data class CurrentLocation(
    val name: String
)

data class OneCallResponse(
    val lat: Double,
    val lon: Double,
    val timezone: String,
    val timezone_offset: Int,

    val current: WeatherHourly,
    val hourly: List<WeatherHourly>,
    val daily: List<WeatherDaily>
)

data class WeatherHourly(
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
    val weather: List<Weather>,
    val pop: Double,
    val rain: Double
)

data class WeatherDaily(
    val dt: Int,
    val sunrise: Int,
    val sunset: Int,
    val moonrise: Int,
    val moonset: Int,
    val moon_phase: Double,
    val temp: Temp,
    val feels_like: FeelsLike,
    val pressure: Int,
    val humidity: Int,
    val dew_point: Double,
    val wind_speed: Double,
    val wind_deg: Int,
    val wind_gust: Double,
    val weather: List<Weather>,
    val clouds: Int,
    val pop: Double,
    val rain: Double,
    val uvi: Double
)

data class FeelsLike(
    val day: Double,
    val night: Double,
    val eve: Double,
    val morn: Double
)

data class Temp(
    val day: Double,
    val min: Double,
    val max: Double,
    val night: Double,
    val eve: Double,
    val morn: Double
)

data class Weather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)


data class AirPollutionResponse(
    val list: List<AirPollution>
)

data class AirPollution(val main: AirPollutionMain, val components: AirPollutionComponents, val dt: Int)
data class AirPollutionComponents(val co: Double, val no: Double, val no2: Double, val o3: Double, val so2: Double, val pm2_5: Double, val pm10: Double, val nh3: Double)

data class AirPollutionMain(val aqi: Int)

interface WeatherServiceApi {

    @GET("/geo/1.0/reverse")
    suspend fun getReverseLocation(
        @Query("lat") latitude: String = lat,
        @Query("lon") longitude: String = lon,
        @Query("limit") limit: Int = 1,
        @Query("appid") appid: String = WeatherServiceApi.appid,
    ): Response<List<CurrentLocation>>

    @GET("/data/2.5/onecall")
    suspend fun getWeather(
        @Query("lat") latitude: String = lat,
        @Query("lon") longitude: String = lon,
        @Query("exclude") exclude: String = "alerts",
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "pl",
        @Query("appid") appid: String = WeatherServiceApi.appid,
    ): Response<OneCallResponse>

    @GET("/data/2.5/air_pollution")
    suspend fun getAirPollution(
        @Query("lat") latitude: String = lat,
        @Query("lon") longitude: String = lon,
        @Query("appid") appid: String = WeatherServiceApi.appid,
    ): Response<AirPollutionResponse>

    companion object {
        private const val BASE_URL = "http://api.openweathermap.org/"
        private const val appid = "8c27af4c68a4a3cc4c9d8d009ece6201"
        private const val lat = "52.335833"
        private const val lon = "16.807778"

        fun create(): WeatherServiceApi {
            val logger =
                HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WeatherServiceApi::class.java)
        }
    }
}