package net.dev.weather.ui.search

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import net.dev.weather.R
import net.dev.weather.data.LocationRepository
import net.dev.weather.data.Place
import net.dev.weather.data.Suggestion
import net.dev.weather.utils.Async
import javax.inject.Inject

data class SearchUiState(
    val query: String? = null,
    val suggestions: List<Suggestion>? = null,
    val isLoading: Boolean = false,
    @StringRes val userMessage: Int? = null
)

@HiltViewModel
class SearchViewModel @Inject constructor(private val locationRepository: LocationRepository) : ViewModel() {
    private val _userMessage: MutableStateFlow<Int?> = MutableStateFlow(null)

    private val _searchText: MutableStateFlow<String> = MutableStateFlow("")

    private val _savedPlaces: Flow<List<Place>> = locationRepository.savedPlaces

    private val _matchedCities: Flow<Async<List<Suggestion>>> = combine(_searchText) { searchText ->
        locationRepository.getSuggestions(searchText.last()).last()
    }.combine(_savedPlaces) { suggestions, savedPlaces ->
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

    suspend fun toggleFavorite(suggestion: Suggestion) {
        locationRepository.toggleFavorite(suggestion)
    }

    fun onClearClick() {
        _searchText.value = ""
    }

    suspend fun setPlaceFromSuggestion(suggestion: Suggestion) {
        locationRepository.setCurrentPlace(suggestion)
    }
}

