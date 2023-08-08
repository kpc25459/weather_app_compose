package net.dev.weather.data.model

data class UserData(
    val currentMode: PlaceMode,
    val currentDeviceLocation: LatandLong?,
    val currentPlace: Place?,
    val favorites: List<Place>
)
