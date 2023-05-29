package net.dev.weather.ui.places

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import net.dev.weather.R
import net.dev.weather.data.Place
import net.dev.weather.data.PlaceMode
import net.dev.weather.data.currentLocation
import net.dev.weather.repositories.SettingsRepository
import net.dev.weather.utils.Async
import javax.inject.Inject

data class PlacesUiState(
    val places: List<Place>? = null,
    val currentPlaceId : String? = null,
    val isLoading: Boolean = false,
    @StringRes val userMessage: Int? = null
)

@HiltViewModel
class PlacesViewModel @Inject constructor(private val settingsRepository: SettingsRepository) : ViewModel() {
    private val _userMessage: MutableStateFlow<Int?> = MutableStateFlow(null)

    private val _places: Flow<Async<List<Place>>> =
        combine(flowOf(listOf(currentLocation)), settingsRepository.favorites) { currentPlace, places -> currentPlace + places }
            .map { Async.Success(it) }
            .catch<Async<List<Place>>> { emit(Async.Error(R.string.loading_error, it)) }

    private val _currentPlace = settingsRepository.currentPlace

    val uiState: StateFlow<PlacesUiState> = combine(
        _places, _currentPlace, _userMessage
    ) { matchedCities, currentPlace, userMessage ->
        when (matchedCities) {
            is Async.Loading -> PlacesUiState(isLoading = true)
            is Async.Error -> PlacesUiState(userMessage = -1)
            is Async.Success -> PlacesUiState(places = matchedCities.data, currentPlaceId = currentPlace.id, userMessage = userMessage)
        }
    }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PlacesUiState(isLoading = true))


    suspend fun removeFromFavorites(place: Place) {
        settingsRepository.removeFromFavorites(place.id)
    }

    suspend fun setCurrentPlace(place: Place) {
        if (place.id == currentLocation.id) {
            settingsRepository.setCurrentMode(PlaceMode.DEVICE_LOCATION)
        } else {
            settingsRepository.setCurrentMode(PlaceMode.FAVORITES)
            settingsRepository.setCurrentPlace(place)
        }
    }
}


