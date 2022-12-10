package net.dev.weather.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

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