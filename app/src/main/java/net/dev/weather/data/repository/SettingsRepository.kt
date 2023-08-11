package net.dev.weather.data.repository

import kotlinx.coroutines.flow.Flow
import net.dev.weather.data.model.Place
import net.dev.weather.data.model.PlaceMode
import net.dev.weather.data.model.UserData
import net.dev.weather.datastore.PreferencesDataSource
import javax.inject.Inject
import javax.inject.Singleton

interface SettingsRepository {

    val userData: Flow<UserData>

    suspend fun setCurrentMode(mode: PlaceMode)

    suspend fun setCurrentPlace(place: Place)

    suspend fun toggleFavorite(place: Place, favorite: Boolean)

}

@Singleton
class SettingsRepositoryImpl @Inject constructor(private val preferencesDataSource: PreferencesDataSource) : SettingsRepository {

    override val userData: Flow<UserData>
        get() = preferencesDataSource.userData


    override suspend fun setCurrentMode(mode: PlaceMode) = preferencesDataSource.setCurrentMode(mode)
    override suspend fun setCurrentPlace(place: Place) = preferencesDataSource.setCurrentPlace(place)

    override suspend fun toggleFavorite(place: Place, favorite: Boolean) = preferencesDataSource.toggleFavoritePlaceId(place, favorite)
}