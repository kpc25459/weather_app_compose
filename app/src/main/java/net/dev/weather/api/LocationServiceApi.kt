package net.dev.weather.api

import net.dev.weather.BuildConfig
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface LocationServiceApi {

    @GET("/maps/api/place/autocomplete/json")
    suspend fun getSuggestions(
        @Query("input") input: String,
        @Query("types") types: String = "geocode",
        @Query("key") apiKey: String = BuildConfig.GOOGLE_API_KEY,
        @Query("sessiontoken") sessiontoken: String = sessionToken,
    ): Response<AutocompleteResult>


    @GET("/maps/api/geocode/json")
    suspend fun getLatLongFromGoogle(
        @Query("place_id") placeId: String,
        @Query("key") apiKey: String = BuildConfig.GOOGLE_API_KEY,
        @Query("sessiontoken") sessiontoken: String = sessionToken,
    ): Response<GetLatLongFromGoogleResult>

    companion object {
        const val BASE_URL = "https://maps.googleapis.com/"
        const val sessionToken = "8c27af4c68a4a3cc4c9d8d009ece6201"
    }
}

data class AutocompleteResult(
    val predictions: List<Prediction>,
)

data class Prediction(
    val description: String,
    val place_id: String,
    val structured_formatting: StructuredFormatting,
)

data class StructuredFormatting(val main_text: String, val secondary_text: String = "")


data class GetLatLongFromGoogleResult(val results: List<GetLatLongFromGoogle>) {
    data class GetLatLongFromGoogle(val geometry: Geometry) {
        data class Geometry(val location: Location) {
            data class Location(val lat: Double, val lng: Double)
        }
    }
}