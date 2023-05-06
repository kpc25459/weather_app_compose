package net.dev.weather.ui.airQuality

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import net.dev.weather.R
import net.dev.weather.data.*
import net.dev.weather.ui.model.*
import net.dev.weather.utils.Async
import javax.inject.Inject
import kotlin.math.roundToInt

data class AirQualityUiState(
    val airQuality: AirQuality? = null,
    val isLoading: Boolean = false,
    @StringRes val userMessage: Int? = null
)

@HiltViewModel
class AirQualityViewModel @Inject constructor(weatherRepository: WeatherRepository, locationRepository: LocationRepository) : ViewModel() {

    private val _userMessage: MutableStateFlow<Int?> = MutableStateFlow(null)

    private val _airQualityFlow: Flow<Async<AirQuality>> =
        combine(
            locationRepository.locationName,
            weatherRepository.airPollutionForecast,
        ) { location, airPollutionForecast ->
            return@combine AirQuality(
                location = location,
                airPollutionForecast = mapToUiModel(airPollutionForecast),
            )
        }
            .map { Async.Success(it) }
            .catch<Async<AirQuality>> { emit(Async.Error(R.string.loading_error, it)) }

    val uiState: StateFlow<AirQualityUiState> = combine(
        _airQualityFlow, _userMessage
    ) { airQuality, userMessage ->
        when (airQuality) {
            is Async.Loading -> AirQualityUiState(isLoading = true)
            is Async.Error -> AirQualityUiState(userMessage = airQuality.message)
            is Async.Success -> AirQualityUiState(airQuality = airQuality.data, userMessage = userMessage)
        }
    }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AirQualityUiState(isLoading = true))

    private fun mapToUiModel(weather: List<AirPollutionForecast>): List<UiAirPollutionForecast> {
        return weather.map {
            UiAirPollutionForecast(
                dt = it.dt.time.toString(),
                aqi = it.aqi,
                no2 = it.no2.roundToInt().toString(),
                o3 = it.o3.roundToInt().toString(),
                pm2_5 = it.pm2_5,
                pm2_5String = "${it.pm2_5.roundToInt()} μg/m3",
                pm10 = it.pm10,
                pm10String = "${it.pm10.roundToInt()} μg/m3",
            )
        }
    }
}

