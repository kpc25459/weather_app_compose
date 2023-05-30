package net.dev.weather.data

import kotlinx.serialization.Serializable

@Serializable
data class Place(val name: String, val id: String, val description: String, val latitude: Double, val longitude: Double)
