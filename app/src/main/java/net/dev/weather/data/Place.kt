package net.dev.weather.data

data class Place(val name: String, val id: String, val description: String, val latitude: Double, val longitude: Double, val isSelected: Boolean = false)
