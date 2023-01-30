package net.dev.weather.ui.currentWeather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import net.dev.weather.data.WeatherCurrent
import net.dev.weather.data.WeatherDaily
import net.dev.weather.data.WeatherHourly
import net.dev.weather.data.WeatherRepository

class CurrentWeatherViewModel(weatherRepository: WeatherRepository) : ViewModel() {

    //TODO: jak łączyć flowy?
    val uiState: StateFlow<CurrentWeatherUiState> = weatherRepository
        .weatherCurrent
        .combine(weatherRepository.weatherDaily) { current, daily ->
            return@combine MainWeather(
                current = current,
                daily = daily.take(7)
            )
        }.combine(weatherRepository.weatherHourly) { main, hourly ->
            return@combine main.copy(hourlyForecast = hourly.take(24))
        }
        .combine(weatherRepository.location) { main, location ->
            return@combine main.copy(location = location)
        }
        .combine(weatherRepository.airQuality) { main, airQuality ->
            return@combine main.copy(airQuality = airQuality)
        }
        .map(CurrentWeatherUiState::Success)
        .catch { CurrentWeatherUiState.Error(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), CurrentWeatherUiState.Loading)
}

sealed interface CurrentWeatherUiState {
    object Loading : CurrentWeatherUiState
    data class Success(val data: MainWeather) : CurrentWeatherUiState
    data class Error(val throwable: Throwable) : CurrentWeatherUiState
}

data class MainWeather(
    val location: String = "",
    val current: WeatherCurrent,
    val daily: List<WeatherDaily>,
    val hourlyForecast: List<WeatherHourly> = emptyList(),
    val airQuality: Int = 0
)
