package kotlin.net.dev.weather.data.repository

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlin.net.dev.weather.data.model.Place
import kotlin.net.dev.weather.data.model.PlaceMode
import kotlin.net.dev.weather.data.model.deviceCurrentLocationPlace
import kotlin.net.dev.weather.network.api.WeatherServiceApi
import javax.inject.Inject
import javax.inject.Singleton

interface PlaceRepository {
    val currentPlace: Flow<Place>

    val currentDevicePlace: Flow<Place>
}

@Singleton
class PlaceRepositoryImpl @Inject constructor(
    private val weatherServiceApi: WeatherServiceApi,
    private val settingsRepository: SettingsRepository
) : PlaceRepository {

    override val currentPlace: Flow<Place>
        get() = flow {
            settingsRepository.userData.collect {
                when (it.currentMode) {
                    PlaceMode.DEVICE_LOCATION -> emit(currentDevicePlace.first())
                    PlaceMode.SEARCH -> it.currentPlace?.let { it1 -> emit(it1) }
                    PlaceMode.FAVORITES -> it.currentPlace?.let { it1 -> emit(it1) }
                }
            }
        }

    override val currentDevicePlace: Flow<Place>
        get() = flow {
            settingsRepository.userData.collect {
                val currentDeviceLocation = it.currentDeviceLocation ?: return@collect

                val reverseLocationResponse = weatherServiceApi.getReverseLocation(currentDeviceLocation.latitude, currentDeviceLocation.longitude)
                val name = reverseLocationResponse.body()?.first()?.name ?: "Unknown"
                val value = Place(name, deviceCurrentLocationPlace.id, deviceCurrentLocationPlace.description, currentDeviceLocation.latitude, currentDeviceLocation.longitude)

                Log.d("LocationRepository", "currentDevicePlace: $value")

                emit(value)
            }
        }
}