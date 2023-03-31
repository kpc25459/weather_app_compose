package net.dev.weather.ui.weatherForecast

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import net.dev.weather.R
import net.dev.weather.data.*
import net.dev.weather.ui.model.*
import net.dev.weather.utils.Async
import kotlin.math.roundToInt

data class WeatherForecastUiState(
    val weatherForecast: WeatherForecast? = null,
    val isLoading: Boolean = false,
    @StringRes val userMessage: Int? = null
)

class WeatherForecastViewModel(weatherRepository: WeatherRepository) : ViewModel() {

    private val _userMessage: MutableStateFlow<Int?> = MutableStateFlow(null)

    private val _weatherForecastFlow: Flow<Async<WeatherForecast>> =
        weatherRepository.weather.map { weather ->
            WeatherForecast(
                daily = mapToUiModel(weather)
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

    private fun mapToUiModel(weather: Weather): List<UiWeatherDaily> = weather.daily.map {
        UiWeatherDaily(
            dt = it.dt,
            description = it.description,
            sunrise = it.sunrise.toString().substringBeforeLast(":"),
            sunset = it.sunset.toString().substringBeforeLast(":"),
            temp = "${it.tempDay.roundToInt()}°C / ${it.tempNight.roundToInt()}°C",
            pressure = "${it.pressure} hPa",
            humidity = "${it.humidity} %",
            wind = "${it.wind.roundToInt()} km/h ${it.windDirection}",
            rain = "${it.rain.roundToInt()} mm/24h",
            uvi = it.uvi.roundToInt().toString(),
            icon = it.icon
        )
    }
}

