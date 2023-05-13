package net.dev.weather.ui.places

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import net.dev.weather.R
import net.dev.weather.data.LocationRepository
import net.dev.weather.data.Place
import net.dev.weather.utils.Async
import javax.inject.Inject

data class PlacesUiState(
    val places: List<Place>? = null,
    val isLoading: Boolean = false,
    @StringRes val userMessage: Int? = null
)

@HiltViewModel
class PlacesViewModel @Inject constructor(private val locationRepository: LocationRepository) : ViewModel() {
    private val _userMessage: MutableStateFlow<Int?> = MutableStateFlow(null)

    private val _places: Flow<Async<List<Place>>> =
        combine(flowOf(listOf(currentLocation)), locationRepository.savedPlaces) { currentPlace, savedPlaces -> currentPlace + savedPlaces }
            .map { Async.Success(it) }
            .catch<Async<List<Place>>> { emit(Async.Error(R.string.loading_error, it)) }

    val uiState: StateFlow<PlacesUiState> = combine(
        _places, _userMessage
    ) { matchedCities, userMessage ->
        when (matchedCities) {
            is Async.Loading -> PlacesUiState(isLoading = true)
            is Async.Error -> PlacesUiState(userMessage = -1)
            is Async.Success -> PlacesUiState(places = matchedCities.data, userMessage = userMessage)
        }
    }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PlacesUiState(isLoading = true))


    suspend fun removePlace(place: Place) {
        locationRepository.removeFromFavorites(place.id)
    }

    suspend fun setCurrentPlace(place: Place) {
        locationRepository.setCurrentPlace(place)
    }
}

val currentLocation = Place("Bieżąca lokalizacja", "-1", "", 0.0, 0.0)
