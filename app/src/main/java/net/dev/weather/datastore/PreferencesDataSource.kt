package net.dev.weather.datastore

import android.util.Log
import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.map
import net.dev.weather.data.model.LatandLong
import net.dev.weather.data.model.Place
import net.dev.weather.data.model.PlaceMode
import net.dev.weather.data.model.UserData
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
                currentDeviceLocation = LatandLong(it.currentDeviceLocation.latitude, it.currentDeviceLocation.longitude),
                currentPlace = Place(it.currentPlace.name, it.currentPlace.id, it.currentPlace.description, it.currentPlace.location.latitude, it.currentPlace.location.longitude),
                favorites = it.favoritesList.map { place ->
                    Place(place.name, place.id, place.description, place.location.latitude, place.location.longitude)
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

    suspend fun setFavorites() {
        try {

        } catch (e: IOException) {
            Log.e("Preferences", "Error updating settings", e)
        }
    }

}