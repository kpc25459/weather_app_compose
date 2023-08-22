package net.dev.weather.ui.currentWeather

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
import net.dev.weather.data.repository.PlaceRepository
import net.dev.weather.data.repository.WeatherRepository
import net.dev.weather.ui.model.PlaceWithCurrentWeather
import net.dev.weather.utils.Async
import javax.inject.Inject

data class CurrentWeatherUiState(
    val placeWithCurrentWeather: PlaceWithCurrentWeather? = null,
    val isLoading: Boolean = false,
    @StringRes val userMessage: Int? = null
)

@HiltViewModel
class CurrentWeatherViewModel @Inject constructor(placeRepository: PlaceRepository, weatherRepository: WeatherRepository) : ViewModel() {

    private val _userMessage: MutableStateFlow<Int?> = MutableStateFlow(null)

    private val _currentWeather: Flow<Async<PlaceWithCurrentWeather>> =
        combine(
            placeRepository.currentPlace,
            weatherRepository.weather,
            weatherRepository.airPollutionCurrent
        ) { currentPlace, weather, airPollutionCurrent ->

            Log.i("CurrentWeatherViewModel", "currentPlace: $currentPlace")

            return@combine PlaceWithCurrentWeather(
                place = currentPlace.name,
                weather = weather,
                airPollutionCurrent = airPollutionCurrent
            )
        }
            .map { Async.Success(it) }
            .catch<Async<PlaceWithCurrentWeather>> { emit(Async.Error(R.string.loading_error, it)) }

    val uiState: StateFlow<CurrentWeatherUiState> = combine(
        _currentWeather, _userMessage
    ) { data, userMessage ->
        when (data) {
            is Async.Loading -> CurrentWeatherUiState(isLoading = true)
            is Async.Error -> CurrentWeatherUiState(userMessage = data.message)
            is Async.Success -> CurrentWeatherUiState(placeWithCurrentWeather = data.data, userMessage = userMessage)
        }
    }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), CurrentWeatherUiState(isLoading = true))
}

