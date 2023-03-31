package net.dev.weather.ui.search

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import net.dev.weather.data.Search
import net.dev.weather.data.WeatherRepository

data class SearchUiState(
    val query: Search? = null,
    val isLoading: Boolean = false,
    @StringRes val userMessage: Int? = null
)

class SearchViewModel(weatherRepository: WeatherRepository) : ViewModel() {

    private val _userMessage: MutableStateFlow<Int?> = MutableStateFlow(null)


}

