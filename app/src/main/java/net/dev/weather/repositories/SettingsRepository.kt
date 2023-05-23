package net.dev.weather.repositories

import android.content.Context
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import net.dev.weather.Location
import net.dev.weather.data.LatandLong
import net.dev.weather.data.Place
import net.dev.weather.settingsDataStore
import javax.inject.Inject
import javax.inject.Singleton

interface SettingsRepository {

    val currentDeviceLocation: Flow<LatandLong?>

    val currentPlace: Flow<Place>

    val favorites: Flow<List<Place>>

    suspend fun clearFavorites()

    suspend fun toggleFavorite(place: Place)
    suspend fun removeFromFavorites(placeId: String)

    suspend fun setCurrentPlace(place: Place)
}

@Singleton
class SettingsRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : SettingsRepository {

    override val currentDeviceLocation: Flow<LatandLong?>
        get() = flow {
            context.settingsDataStore.data.map { settings -> settings.currentLocation }.collect { currentLocation ->
                if (currentLocation != null) {
                    Log.d("LocationRepository", "emit location: $currentLocation")
                    emit(LatandLong(latitude = currentLocation.latitude, longitude = currentLocation.longitude))
                } else {
                    emit(null)
                }
            }
        }

    override val currentPlace: Flow<Place>
        get() = flow {
            context.settingsDataStore.data.map { settings -> settings.currentPlace }.collect { currentPlace ->
                currentPlace?.let {
                    emit(
                        Place(
                            name = currentPlace.name,
                            id = currentPlace.id,
                            description = currentPlace.description,
                            latitude = currentPlace.location.latitude,
                            longitude = currentPlace.location.longitude
                        )
                    )
                }
            }
        }

    override val favorites: Flow<List<Place>>
        get() = flow {
            context.settingsDataStore.data.map { settings ->
                settings.favoritesList.map { place ->
                    Place(place.name, place.id, place.description, place.location.latitude, place.location.longitude)
                }
            }.collect { places ->
                emit(places)
            }
        }

    override suspend fun clearFavorites() {
        context.settingsDataStore.updateData { currentSettings ->
            currentSettings.toBuilder().clearFavorites().build()
        }
    }

    //TODO: add favorites?

    override suspend fun removeFromFavorites(placeId: String) {
        context.settingsDataStore.updateData { currentSettings ->
            currentSettings.toBuilder().clearFavorites().addAllFavorites(currentSettings.favoritesList.filter { it.id != placeId }).build()
        }
    }

    override suspend fun toggleFavorite(place: Place) {
        context.settingsDataStore.updateData { currentSettings ->
            val p = currentSettings.favoritesList.find { it.id == place.id }
            if (p != null) {
                currentSettings.toBuilder().clearFavorites().addAllFavorites(currentSettings.favoritesList.filter { it.id != place.id }).build()
            } else {
                currentSettings.toBuilder().addFavorites(place.toProto()).build()
            }
        }
    }

    override suspend fun setCurrentPlace(place: Place) {
        context.settingsDataStore.updateData { currentSettings ->
            currentSettings.toBuilder().setCurrentPlace(place.toProto()).build()
        }
    }

    private fun Place.toProto(): net.dev.weather.Place {
        return net.dev.weather.Place.newBuilder().also {
            it.id = id
            it.name = name
            it.description = description
            it.location = Location.newBuilder().also { builder ->
                builder.latitude = latitude
                builder.longitude = longitude
            }.build()
        }.build()
    }
}


