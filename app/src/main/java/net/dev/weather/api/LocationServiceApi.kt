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

data class GetWeatherResponse(
    val lat: Double,
    val lon: Double,
    val timezone: String,
    val timezone_offset: Int,

    val current: WeatherHourly,
    val hourly: List<WeatherHourly>
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
    val pop: Double
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

data class AirPollution(val main: Main, val components: Components, val dt: Int)
data class Components(val co: Double, val no: Double, val no2: Double, val o3: Double, val so2: Double, val pm2_5: Double, val pm10: Double, val nh3: Double)

data class Main(val aqi: Int)

interface LocationServiceApi {

    @GET("/geo/1.0/reverse")
    suspend fun getReverseLocation(
        @Query("lat") latitude: String = lat,
        @Query("lon") longitude: String = lon,
        @Query("limit") limit: Int = 1,
        @Query("appid") appid: String = LocationServiceApi.appid,
    ): Response<List<CurrentLocation>>

    @GET("/data/2.5/onecall")
    suspend fun getWeather(
        @Query("lat") latitude: String = lat,
        @Query("lon") longitude: String = lon,
        @Query("exclude") exclude: String = "minutely,daily,alerts",
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "pl",
        @Query("appid") appid: String = LocationServiceApi.appid,
    ): Response<GetWeatherResponse>

    @GET("/data/2.5/air_pollution")
    suspend fun getAirPollution(
        @Query("lat") latitude: String = lat,
        @Query("lon") longitude: String = lon,
        @Query("appid") appid: String = LocationServiceApi.appid,
    ): Response<AirPollutionResponse>

    companion object {
        private const val BASE_URL = "http://api.openweathermap.org/"
        private const val appid = "8c27af4c68a4a3cc4c9d8d009ece6201"
        private const val lat = "52.335833"
        private const val lon = "16.807778"

        fun create(): LocationServiceApi {
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
                .create(LocationServiceApi::class.java)
        }
    }
}