package net.dev.weather.ui.weatherForecast

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import net.dev.weather.data.WeatherDaily
import net.dev.weather.data.WeatherRepository
import net.dev.weather.ui.weatherForecast.WeatherForecastUiState.Success
import net.dev.weather.ui.weatherForecast.WeatherForecastUiState.Error

class WeatherForecastViewModel(weatherRepository: WeatherRepository) : ViewModel() {
    val uiState: StateFlow<WeatherForecastUiState> = weatherRepository
        .dailyWeather
        .take(7)
        .map(::Success)
        .catch { Error(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), WeatherForecastUiState.Loading)
}


sealed interface WeatherForecastUiState {
    object Loading : WeatherForecastUiState
    data class Success(val data: List<WeatherDaily>) : WeatherForecastUiState
    data class Error(val throwable: Throwable) : WeatherForecastUiState
}