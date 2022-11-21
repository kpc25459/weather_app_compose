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

interface LocationServiceApi {

    @GET("/geo/1.0/reverse")
    suspend fun getReverseLocation(
        @Query("lat") latitude: String,
        @Query("lon") longitude: String,
        @Query("limit") limit: Int = 1,
        @Query("appid") appid: String = "8c27af4c68a4a3cc4c9d8d009ece6201",
    ): Response<List<CurrentLocation>>

    companion object {
        private const val BASE_URL = "http://api.openweathermap.org/"

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