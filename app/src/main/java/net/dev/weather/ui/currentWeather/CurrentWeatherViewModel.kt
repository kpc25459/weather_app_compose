package net.dev.weather.ui.currentWeather

import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.SavedStateHandle
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
import net.dev.weather.CurrentWeather
import net.dev.weather.R
import net.dev.weather.data.model.LatandLong
import net.dev.weather.data.model.Place
import net.dev.weather.data.repository.LocationRepository
import net.dev.weather.data.repository.PlaceRepository
import net.dev.weather.data.repository.WeatherRepository
import net.dev.weather.network.api.WeatherServiceApi
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
    savedStateHandle: SavedStateHandle,
    placeRepository: PlaceRepository,
    weatherRepository: WeatherRepository,
    locationRepository: LocationRepository,
    weatherServiceApi: WeatherServiceApi
) :
    ViewModel() {

    private val _placeId = savedStateHandle.getStateFlow(CurrentWeather.placeIdArg, "")

    //TODO: to będzie na warstwie domain (use case) - będzie używane z innych widoków
    private val _placeFlow: Flow<Async<Place>> = _placeId.map {
        Log.i("CurrentWeatherViewModel", "placeId: $it")

        val latAndLong: LatandLong = locationRepository.getLocationFromGoogle(it)

        val reverseLocationResponse = weatherServiceApi.getReverseLocation(latAndLong.latitude, latAndLong.longitude)
        val name = reverseLocationResponse.body()?.first()?.name ?: "Unknown"

        Place(name, it, "", latAndLong.latitude, latAndLong.longitude)
    }
        //TODO: coś w tym stylu
        //.onStart { emit(Async.Loading) }
        .map { Async.Success(it) }
        .catch<Async<Place>> { emit(Async.Error(R.string.loading_error, it)) }


    private val _weatherFlow: Flow<Async<PlaceWithCurrentWeather>> = _placeFlow.map {
        if (it is Async.Success)
            PlaceWithCurrentWeather(it.data.name, weatherRepository.weatherFor(it.data.latitude, it.data.longitude))
        else
            throw Exception("PlaceFlow is not success")
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