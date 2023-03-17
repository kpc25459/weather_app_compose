package net.dev.weather.ui.airQuality

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import net.dev.weather.ui.model.UiAirPollutionForecast
import net.dev.weather.data.WeatherRepository

/*
class AirQualityViewModel(weatherRepository: WeatherRepository) : ViewModel() {

    val uiState: StateFlow<AirQualityUiState> = weatherRepository
        .airPollutionForecast
        .combine(weatherRepository.location) { airPollution, location ->
            return@combine AirPollution(location = location, forecast = airPollution.take(4))
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
    val forecast: List<UiAirPollutionForecast>
)
*/
