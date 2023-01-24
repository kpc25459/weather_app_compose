package net.dev.weather.ui.airQuality

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import net.dev.weather.data.*
import net.dev.weather.fromAqiIndex

class AirQualityViewModel(weatherRepository: WeatherRepository) : ViewModel() {

    val uiState: StateFlow<AirQualityUiState> = weatherRepository
        .airPollution
        .combine(weatherRepository.location) { airPollution, location ->
            return@combine airPollution.copy(location = location)
        }
        .map(AirQualityUiState::Success)
        .catch { AirQualityUiState.Error(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AirQualityUiState.Loading)
}

sealed interface AirQualityUiState {
    object Loading : AirQualityUiState
    data class Success(val data: AirPollution) : AirQualityUiState
    data class Error(val throwable: Throwable) : AirQualityUiState
}



data class AirPollution(
    val location: String = "",
    val forecast: List<AirPollutionForecast>
)
