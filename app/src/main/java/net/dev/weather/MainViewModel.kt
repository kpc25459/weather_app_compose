package net.dev.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import net.dev.weather.data.*

class MainViewModel(weatherRepository: WeatherRepository) : ViewModel() {

    val uiState: StateFlow<MainUiState> = weatherRepository
        .location
        .combine(weatherRepository.weather) { location, weather ->
            return@combine Main(
                location = location,
                current = weather.current,
                daily = weather.daily,
                hourlyForecast = weather.hourly
            )
        }
        .combine(weatherRepository.airPollutionForecast) { main, airPollution ->
            return@combine main.copy(
                airPollutionForecast = airPollution
            )
        }
        .combine(weatherRepository.airQuality) { main, airQuality ->
            return@combine main.copy(
                airQuality = airQuality
            )
        }
        .map(MainUiState::Success)
        .catch { MainUiState.Error(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), MainUiState.Loading)
}

sealed interface MainUiState {
    object Loading : MainUiState
    data class Success(val data: Main) : MainUiState
    data class Error(val throwable: Throwable) : MainUiState
}

data class Main(
    val location: String,
    val current: WeatherCurrent,
    val daily: List<WeatherDaily>,
    val hourlyForecast: List<WeatherHourly>,
    val airQuality: Int = 0,
    val airPollutionForecast: List<AirPollutionForecast> = emptyList()
)
