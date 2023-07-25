package net.dev.weather.ui.airQuality

import android.util.Log
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
import net.dev.weather.data.repository.LocationRepository
import net.dev.weather.data.repository.WeatherRepository
import net.dev.weather.ui.model.PlaceWithAirPollutionForecast
import net.dev.weather.utils.Async
import javax.inject.Inject

data class AirQualityUiState(
    val airQuality: PlaceWithAirPollutionForecast? = null,
    val isLoading: Boolean = false,
    @StringRes val userMessage: Int? = null
)

@HiltViewModel
class AirQualityViewModel @Inject constructor(weatherRepository: WeatherRepository, locationRepository: LocationRepository) : ViewModel() {

    private val _userMessage: MutableStateFlow<Int?> = MutableStateFlow(null)

    private val _airQualityFlow: Flow<Async<PlaceWithAirPollutionForecast>> =
        combine(
            locationRepository.currentPlace,
            weatherRepository.airPollutionForecast,
        ) { currentPlace, airPollutionForecast ->

            Log.i("AirQualityViewModel", "currentPlace: $currentPlace")

            return@combine PlaceWithAirPollutionForecast(
                place = currentPlace,
                airPollutionForecast = airPollutionForecast,
            )
        }
            .map { Async.Success(it) }
            .catch<Async<PlaceWithAirPollutionForecast>> { emit(Async.Error(R.string.loading_error, it)) }

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
}

