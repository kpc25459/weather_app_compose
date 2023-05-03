package net.dev.weather.data

data class Suggestion(val name: String, val id: String, val description: String? = null, val isFavorite: Boolean = false)
