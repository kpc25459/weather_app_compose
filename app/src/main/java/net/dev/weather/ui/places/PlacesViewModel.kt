package net.dev.weather.ui.places

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import net.dev.weather.R
import net.dev.weather.data.model.Place
import net.dev.weather.data.model.PlaceMode
import net.dev.weather.data.model.deviceCurrentLocation
import net.dev.weather.data.repository.SettingsRepository
import net.dev.weather.utils.Async
import javax.inject.Inject

data class PlacesUiState(
    val favorites: List<Place>? = null,
    val currentPlaceId: String? = null,
    val isLoading: Boolean = false,
    @StringRes val userMessage: Int? = null
)

@HiltViewModel
class PlacesViewModel @Inject constructor(private val settingsRepository: SettingsRepository) : ViewModel() {
    private val _userMessage: MutableStateFlow<Int?> = MutableStateFlow(null)

    private val _userData = settingsRepository.userData

    private val _favorites = settingsRepository.userData.map { Async.Success(listOf(deviceCurrentLocation) + it.favorites) }
        .catch<Async<List<Place>>> { emit(Async.Error(R.string.loading_error, it)) }

    val uiState: StateFlow<PlacesUiState> = combine(
        _favorites, _userData, _userMessage
    ) { favorites, userData, userMessage ->
        when (favorites) {
            is Async.Loading -> PlacesUiState(isLoading = true)
            is Async.Error -> PlacesUiState(userMessage = -1)
            is Async.Success -> PlacesUiState(
                favorites = favorites.data,
                currentPlaceId = if (userData.currentMode == PlaceMode.DEVICE_LOCATION) deviceCurrentLocation.id else userData.currentPlace?.id,
                userMessage = userMessage
            )
        }
    }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PlacesUiState(isLoading = true))


    suspend fun removeFromFavorites(place: Place) {
        settingsRepository.toggleFavorite(place, false)
    }

    suspend fun setCurrentPlace(place: Place) {
        if (place.id == deviceCurrentLocation.id) {
            settingsRepository.setCurrentMode(PlaceMode.DEVICE_LOCATION)
        } else {
            settingsRepository.setCurrentMode(PlaceMode.FAVORITES)
            settingsRepository.setCurrentPlace(place)
        }
    }
}