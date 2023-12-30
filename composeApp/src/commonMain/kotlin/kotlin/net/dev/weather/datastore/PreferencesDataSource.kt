package net.dev.weather.datastore

import android.util.Log
import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.map
import kotlin.net.dev.weather.data.model.DarkThemeConfig
import kotlin.net.dev.weather.data.model.LatandLong
import kotlin.net.dev.weather.data.model.Place
import kotlin.net.dev.weather.data.model.PlaceMode
import kotlin.net.dev.weather.data.model.UserData
import java.io.IOException
import javax.inject.Inject

class PreferencesDataSource @Inject constructor(
    private val userPreferences: DataStore<UserPreferences>
) {

    val userData = userPreferences.data
        .map {
            UserData(
                currentMode = when (it.currentMode) {
                    null,
                    net.dev.weather.datastore.PlaceMode.CURRENT_LOCATION -> PlaceMode.DEVICE_LOCATION

                    net.dev.weather.datastore.PlaceMode.FAVORITES -> PlaceMode.FAVORITES
                    net.dev.weather.datastore.PlaceMode.SEARCH -> PlaceMode.SEARCH
                    else -> PlaceMode.DEVICE_LOCATION
                },
                currentDeviceLocation = if (it.currentDeviceLocation.latitude != 0.0 && it.currentDeviceLocation.longitude != 0.0) LatandLong(
                    it.currentDeviceLocation.latitude,
                    it.currentDeviceLocation.longitude
                ) else null,
                currentPlace = Place(it.currentPlace.name, it.currentPlace.id, it.currentPlace.description, it.currentPlace.location.latitude, it.currentPlace.location.longitude),
                favorites = it.favoritesMap.values.map { place ->
                    Place(place.name, place.id, place.description, place.location.latitude, place.location.longitude)
                },
                darkThemeConfig = when (it.darkThemeConfig) {
                    null,
                    DarkThemeConfigProto.DARK_THEME_CONFIG_UNSPECIFIED,
                    DarkThemeConfigProto.UNRECOGNIZED,
                    DarkThemeConfigProto.DARK_THEME_CONFIG_FOLLOW_SYSTEM ->
                        DarkThemeConfig.FOLLOW_SYSTEM

                    DarkThemeConfigProto.DARK_THEME_CONFIG_LIGHT ->
                        DarkThemeConfig.LIGHT

                    DarkThemeConfigProto.DARK_THEME_CONFIG_DARK -> DarkThemeConfig.DARK
                }
            )
        }

    suspend fun setCurrentMode(mode: PlaceMode) {
        try {
            userPreferences.updateData {
                it.copy {
                    this.currentMode = when (mode) {
                        PlaceMode.DEVICE_LOCATION -> net.dev.weather.datastore.PlaceMode.CURRENT_LOCATION
                        PlaceMode.SEARCH -> net.dev.weather.datastore.PlaceMode.SEARCH
                        PlaceMode.FAVORITES -> net.dev.weather.datastore.PlaceMode.FAVORITES
                    }
                }
            }
        } catch (e: IOException) {
            Log.e("Preferences", "Error updating settings", e)
        }
    }

    suspend fun setCurrentPlace(place: Place) {
        try {
            userPreferences.updateData {
                it.copy {
                    this.currentPlace = place {
                        name = place.name
                        id = place.id
                        description = place.description
                        location = location {
                            latitude = place.latitude
                            longitude = place.longitude
                        }
                    }
                }
            }
        } catch (e: IOException) {
            Log.e("Preferences", "Error updating settings", e)
        }
    }

    suspend fun setCurrentDeviceLocation(location: LatandLong) {
        try {
            userPreferences.updateData {
                it.copy {
                    this.currentDeviceLocation = location {
                        latitude = location.latitude
                        longitude = location.longitude
                    }
                }
            }
        } catch (e: IOException) {
            Log.e("Preferences", "Error updating settings", e)
        }
    }

    suspend fun resetCurrentDeviceLocation() {
        try {
            userPreferences.updateData {
                it.copy {
                    this.currentDeviceLocation = location {
                        latitude = 0.0
                        longitude = 0.0
                    }
                }
            }
        } catch (e: IOException) {
            Log.e("Preferences", "Error updating settings", e)
        }
    }

    suspend fun toggleFavoritePlaceId(place: Place, favorite: Boolean) {
        try {
            userPreferences.updateData {
                it.copy {
                    if (favorite) favorites.put(
                        place.id,
                        place {
                            this.id = place.id
                            name = place.name
                            description = place.description
                            location = location {
                                latitude = place.latitude
                                longitude = place.longitude
                            }
                        }) else {
                        favorites.remove(place.id)
                    }
                }
            }
        } catch (e: IOException) {
            Log.e("Preferences", "Error updating settings", e)
        }
    }
}