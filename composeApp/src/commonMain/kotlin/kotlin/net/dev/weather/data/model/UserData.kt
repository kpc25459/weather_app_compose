package kotlin.net.dev.weather.data.model

data class UserData(
    val currentMode: PlaceMode,
    val currentDeviceLocation: LatandLong?,
    val currentPlace: Place?,

    //TODO: tutaj na Set zmienić?
    val favorites: List<Place>,

    val darkThemeConfig: DarkThemeConfig,
)
