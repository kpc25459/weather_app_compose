package net.dev.weather.ui.search

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import net.dev.weather.R
import net.dev.weather.data.WeatherRepository
import net.dev.weather.utils.Async
import javax.inject.Inject

data class SearchUiState(
    val query: String? = null,
    val matchedCities: List<String>? = null,
    val isLoading: Boolean = false,
    @StringRes val userMessage: Int? = null
)

@HiltViewModel
class SearchViewModel @Inject constructor(weatherRepository: WeatherRepository) : ViewModel() {

    private val _allCities: ArrayList<String> = arrayListOf("Poznań", "Warszawa", "Kraków")

    private val _userMessage: MutableStateFlow<Int?> = MutableStateFlow(null)

    private val _searchText: MutableStateFlow<String> = MutableStateFlow("")

    private val _matchedCities: Flow<Async<List<String>>> = flowOf(_allCities)
        .combine(_searchText) { cities, searchText ->
            cities.filter { it.contains(searchText, true) }
        }
        .map { Async.Success(it) }
        .catch<Async<List<String>>> { emit(Async.Error(R.string.loading_error, it)) }

    val uiState: StateFlow<SearchUiState> = combine(
        _matchedCities, _searchText, _userMessage
    ) { matchedCities, searchText, userMessage ->
        when (matchedCities) {
            is Async.Loading -> SearchUiState(isLoading = true)
            is Async.Error -> SearchUiState(userMessage = -1)
            is Async.Success -> SearchUiState(matchedCities = matchedCities.data, query = searchText, userMessage = userMessage)
        }
    }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SearchUiState(isLoading = true))


    fun onSearchTextChanged(newSerachText: String) {
        _searchText.value = newSerachText
    }
}

