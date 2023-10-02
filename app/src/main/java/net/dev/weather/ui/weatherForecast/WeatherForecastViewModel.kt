package net.dev.weather.ui.weatherForecast

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import net.dev.weather.R
import net.dev.weather.data.model.WeatherForecast
import net.dev.weather.data.repository.WeatherRepository
import net.dev.weather.utils.Async
import javax.inject.Inject

data class WeatherForecastUiState(
    val weatherForecast: WeatherForecast? = null,
    val isLoading: Boolean = false,
    @StringRes val userMessage: Int? = null
)

@HiltViewModel
class WeatherForecastViewModel @Inject constructor(weatherRepository: WeatherRepository) : ViewModel() {

    private val _userMessage: MutableStateFlow<Int?> = MutableStateFlow(null)

    private val _weatherForecastFlow: Flow<Async<WeatherForecast>> =
        weatherRepository.weather.map { weather ->
            WeatherForecast(
                daily = weather.daily
            )
        }
            .map { Async.Success(it) }
            .catch<Async<WeatherForecast>> { emit(Async.Error(R.string.loading_error, it)) }


    val uiState: StateFlow<WeatherForecastUiState> = combine(
        _weatherForecastFlow, _userMessage
    ) { WeatherForecast, userMessage ->
        when (WeatherForecast) {
            is Async.Loading -> WeatherForecastUiState(isLoading = true)
            is Async.Error -> WeatherForecastUiState(userMessage = WeatherForecast.message)
            is Async.Success -> WeatherForecastUiState(weatherForecast = WeatherForecast.data, userMessage = userMessage)
        }
    }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), WeatherForecastUiState(isLoading = true))
}

