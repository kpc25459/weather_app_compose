package net.dev.weather.ui.search

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import net.dev.weather.R
import net.dev.weather.data.model.Place
import net.dev.weather.data.model.PlaceMode
import net.dev.weather.data.model.Suggestion
import net.dev.weather.data.repository.LocationRepository
import net.dev.weather.data.repository.SettingsRepository
import net.dev.weather.utils.Async
import javax.inject.Inject

data class SearchUiState(
    val query: String? = null,
    val suggestions: List<Suggestion>? = null,
    val isLoading: Boolean = false,
    @StringRes val userMessage: Int? = null
)

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val locationRepository: LocationRepository,
    private val settingsRepository: SettingsRepository,
) : ViewModel() {
    private val _userMessage: MutableStateFlow<Int?> = MutableStateFlow(null)

    private val _searchText: MutableStateFlow<String> = MutableStateFlow("")

    private val _favorites: Flow<List<Place>> = settingsRepository.favorites

    private val _matchedCities: Flow<Async<List<Suggestion>>> = combine(_searchText) { searchText ->
        locationRepository.getSuggestions(searchText.last()).last()
    }.combine(_favorites) { suggestions, savedPlaces ->
        suggestions.map { suggestion ->
            suggestion.copy(isFavorite = savedPlaces.any { it.id == suggestion.id })
        }
    }
        .map { Async.Success(it) }
        .catch<Async<List<Suggestion>>> { emit(Async.Error(R.string.loading_error, it)) }

    val uiState: StateFlow<SearchUiState> = combine(
        _matchedCities, _searchText, _userMessage
    ) { matchedCities, searchText, userMessage ->
        when (matchedCities) {
            is Async.Loading -> SearchUiState(isLoading = true)
            is Async.Error -> SearchUiState(userMessage = -1)
            is Async.Success -> SearchUiState(suggestions = matchedCities.data, query = searchText, userMessage = userMessage)
        }
    }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SearchUiState(isLoading = true))


    fun onSearchTextChanged(newSerachText: String) {
        _searchText.value = newSerachText
    }

    fun onClearClick() {
        _searchText.value = ""
    }

    suspend fun toggleFavorite(suggestion: Suggestion) {
        settingsRepository.toggleFavorite(suggestion.toPlace())
    }

    suspend fun setCurrentPlace(suggestion: Suggestion) {
        settingsRepository.setCurrentMode(PlaceMode.FAVORITES)
        settingsRepository.setCurrentPlace(suggestion.toPlace())
    }

    private suspend fun Suggestion.toPlace(): Place {
        val location = locationRepository.getLocationFromGoogle(this.id)

        return Place(
            name = name,
            id = id,
            description = description ?: "",
            latitude = location.latitude,
            longitude = location.longitude
        )
    }
}



