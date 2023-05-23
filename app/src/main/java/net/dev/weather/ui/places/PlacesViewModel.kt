package net.dev.weather.ui.places

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import net.dev.weather.R
import net.dev.weather.data.Place
import net.dev.weather.repositories.SettingsRepository
import net.dev.weather.utils.Async
import javax.inject.Inject

data class PlacesUiState(
    val places: List<Place>? = null,
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


    suspend fun removeFromFavorites(place: Place) {
        settingsRepository.removeFromFavorites(place.id)
    }

    suspend fun setCurrentPlace(place: Place) {
        //TODO: place z bieżącej lokalizacji
        /*val place2 = if (place.id == currentLocation.id) {
            buildProtoPlaceFromPlace(buildFromCurrentLocation())
        } else {
            buildProtoPlaceFromPlace(place)
        }*/

        settingsRepository.setCurrentPlace(place)
    }

    /*private suspend fun buildFromCurrentLocation(): Place {
        val lastLocation = context.settingsDataStore.data.map { settings -> settings.currentLocation }.first()
        Log.d("LocationRepository", "buildFromCurrentLocation: $lastLocation")

        val reverseLocationResponse = weatherServiceApi.getReverseLocation(lastLocation.latitude, lastLocation.longitude)
        val name = reverseLocationResponse.body()?.first()?.name ?: "Unknown"
        return Place(name, currentLocation.id, currentLocation.description, lastLocation.latitude, lastLocation.longitude)
    }*/
}

val currentLocation = Place("Bieżąca lokalizacja", "-1", "", 0.0, 0.0)
