package net.dev.weather.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface LocationServiceApi {

    @GET("/maps/api/place/autocomplete/json")
    suspend fun getSuggestions(
        @Query("input") input: String,
        @Query("key") apiKey: String = googlePlacesApiKey,
        @Query("sessiontoken") sessiontoken: String = sessionToken,
    ): Response<AutocompleteResult>


    /*   @GET("/geocode/json")
       suspend fun getLatLongFromGoogle(
           @Query("place_id") placeId: String,
           @Query("key") apiKey: String = googlePlacesApiKey,
           @Query("sessiontoken") sessiontoken: String = sessionToken,
       ): Response<List<CurrentLocation>>*/

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
)