package net.dev.weather.ui.weatherForecast

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import net.dev.weather.R
import kotlin.net.dev.weather.data.model.WeatherForecast
import kotlin.net.dev.weather.data.repository.PlaceRepository
import kotlin.net.dev.weather.data.repository.WeatherRepository
import net.dev.weather.utils.Async
import javax.inject.Inject

data class WeatherForecastUiState(
    val weatherForecast: WeatherForecast? = null,
    val isLoading: Boolean = false,
    @StringRes val userMessage: Int? = null
)

@HiltViewModel
class WeatherForecastViewModel @Inject constructor(
    weatherRepository: WeatherRepository,
    placeRepository: PlaceRepository,
) : ViewModel() {

    private val _userMessage: MutableStateFlow<Int?> = MutableStateFlow(null)

    private val _weatherForecastFlow: Flow<Async<WeatherForecast>> = placeRepository.currentPlace.map {
        WeatherForecast(daily = weatherRepository.weatherFor(it.latitude, it.longitude).daily)
    }
        .map { Async.Success(it) }
        .catch<Async<WeatherForecast>> { emit(Async.Error(R.string.loading_error, it)) }

    val uiState: StateFlow<WeatherForecastUiState> = combine(
        _weatherForecastFlow, _userMessage
    ) { weatherForecast, userMessage ->
        when (weatherForecast) {
            is Async.Loading -> WeatherForecastUiState(isLoading = true)
            is Async.Error -> WeatherForecastUiState(userMessage = weatherForecast.message)
            is Async.Success -> WeatherForecastUiState(weatherForecast = weatherForecast.data, userMessage = userMessage)
        }
    }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), WeatherForecastUiState(isLoading = true))
}