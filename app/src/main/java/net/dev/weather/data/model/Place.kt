package net.dev.weather.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Place(val name: String, val id: String, val description: String, val latitude: Double, val longitude: Double)

val deviceCurrentLocation = Place("Bieżąca lokalizacja", "-1", "", 0.0, 0.0)