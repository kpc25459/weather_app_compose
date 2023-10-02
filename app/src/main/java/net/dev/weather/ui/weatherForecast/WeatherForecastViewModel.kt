package net.dev.weather.ui.weatherForecast

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
import net.dev.weather.data.model.WeatherForecast
import net.dev.weather.data.repository.LocationRepository
import net.dev.weather.data.repository.WeatherRepository
import net.dev.weather.network.api.WeatherServiceApi
import net.dev.weather.utils.Async
import javax.inject.Inject

data class WeatherForecastUiState(
    val weatherForecast: WeatherForecast? = null,
    val isLoading: Boolean = false,
    @StringRes val userMessage: Int? = null
)

@HiltViewModel
class WeatherForecastViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    weatherRepository: WeatherRepository,
    locationRepository: LocationRepository,
    weatherServiceApi: WeatherServiceApi
) : ViewModel() {

    private val _placeId = savedStateHandle.getStateFlow(CurrentWeather.placeIdArg, "")

    private val _userMessage: MutableStateFlow<Int?> = MutableStateFlow(null)


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

    /* private val _weatherForecastFlow: Flow<Async<WeatherForecast>> =
         weatherRepository.weather.map { weather ->
             WeatherForecast(
                 daily = weather.daily
             )
         }
             .map { Async.Success(it) }
             .catch<Async<WeatherForecast>> { emit(Async.Error(R.string.loading_error, it)) }*/

    private val _weatherForecastFlow: Flow<Async<WeatherForecast>> = _placeFlow.map {
        if (it is Async.Success)
            WeatherForecast(
                daily = weatherRepository.weatherFor(it.data.latitude, it.data.longitude).daily
            )
        else
            throw Exception("PlaceFlow is not success")


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

