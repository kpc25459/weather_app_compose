package net.dev.weather.ui.currentWeather

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
class CurrentWeatherViewModel @Inject constructor(
    placeRepository: PlaceRepository,
    weatherRepository: WeatherRepository
) :
    ViewModel() {

    private val _weatherFlow: Flow<Async<PlaceWithCurrentWeather>> = placeRepository.currentPlace.map {
        PlaceWithCurrentWeather(it.name, weatherRepository.weatherFor(it.latitude, it.longitude))
    }
        .map { Async.Success(it) }
        .catch<Async<PlaceWithCurrentWeather>> { emit(Async.Error(R.string.loading_error, it)) }

    private val _userMessage: MutableStateFlow<Int?> = MutableStateFlow(null)

    val uiState: StateFlow<CurrentWeatherUiState> = combine(
        _weatherFlow, _userMessage
    ) { weatherFlow, userMessage ->

        when (weatherFlow) {
            is Async.Loading -> CurrentWeatherUiState(isLoading = true)
            is Async.Error -> CurrentWeatherUiState(userMessage = weatherFlow.message)
            is Async.Success -> CurrentWeatherUiState(placeWithCurrentWeather = weatherFlow.data, userMessage = userMessage)
        }
    }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), CurrentWeatherUiState(isLoading = true))
}