package net.dev.weather.ui.currentWeather

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import net.dev.weather.R
import net.dev.weather.data.repository.PlaceRepository
import net.dev.weather.data.repository.WeatherRepository
import net.dev.weather.network.api.OneCallResponse
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

    private val client by lazy {
        HttpClient {
            install(ContentNegotiation) {
                json()
            }

            install(HttpTimeout) {
                requestTimeoutMillis = 5000
            }
        }
    }

    fun getWeather() {


        try {

            // val response = client.get<OneCallResponse>("http://api.openweathermap.org/data/3.0/onecall?lat=50.8323933&lon=15.518745&exclude=alerts&units=metric&lang=pl&appid=9f7dc5b681f73aa88171c3a99b9037be")

            viewModelScope.launch {
                val response: OneCallResponse =
                    client.get("http://api.openweathermap.org/data/3.0/onecall?lat=50.8323933&lon=15.518745&exclude=alerts&units=metric&lang=pl&appid=9f7dc5b681f73aa88171c3a99b9037be").let {
                        it.body()
                    }

                println(response)
            }

        } catch (e: Throwable) {

        }


    }


    private val _weatherFlow: Flow<Async<PlaceWithCurrentWeather>> = placeRepository.currentPlace.map {
        PlaceWithCurrentWeather(it.name, weatherRepository.weatherFor(it.latitude, it.longitude))
    }
        .map {
            getWeather()
            Async.Success(it)

        }
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