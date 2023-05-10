package net.dev.weather.ui.currentWeather

import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import net.dev.weather.R
import net.dev.weather.data.CurrentWeather
import net.dev.weather.data.LocationRepository
import net.dev.weather.data.Weather
import net.dev.weather.data.WeatherRepository
import net.dev.weather.ui.model.UiWeather
import net.dev.weather.ui.model.UiWeatherCurrent
import net.dev.weather.ui.model.UiWeatherDaily
import net.dev.weather.ui.model.UiWeatherHourly
import net.dev.weather.utils.Async
import javax.inject.Inject
import kotlin.math.roundToInt

data class CurrentWeatherUiState(
    val main: CurrentWeather? = null,
    val isLoading: Boolean = false,
    @StringRes val userMessage: Int? = null
)

@HiltViewModel
class CurrentWeatherViewModel @Inject constructor(weatherRepository: WeatherRepository, locationRepository: LocationRepository) : ViewModel() {

    private val _userMessage: MutableStateFlow<Int?> = MutableStateFlow(null)

    private val _currentWeather: Flow<Async<CurrentWeather>> =
        combine(locationRepository.location,
            locationRepository.locationName,
            weatherRepository.weather,
            weatherRepository.airPollutionCurrent
        ) { location, locationName, weather, airPollutionCurrent ->

            Log.i("CurrentWeatherViewModel", "location: $location")

            val uiWeather = mapToUiModel(weather)
            return@combine CurrentWeather(
                location = locationName,
                current = uiWeather.current,
                daily = uiWeather.daily,
                hourlyForecast = uiWeather.hourly,
                airPollutionCurrent = airPollutionCurrent
            )
        }
            .map { Async.Success(it) }
            .catch<Async<CurrentWeather>> { emit(Async.Error(R.string.loading_error, it)) }

    val uiState: StateFlow<CurrentWeatherUiState> = combine(
        _currentWeather, _userMessage
    ) { data, userMessage ->
        when (data) {
            is Async.Loading -> CurrentWeatherUiState(isLoading = true)
            is Async.Error -> CurrentWeatherUiState(userMessage = data.message)
            is Async.Success -> CurrentWeatherUiState(main = data.data, userMessage = userMessage)
        }
    }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), CurrentWeatherUiState(isLoading = true))


    private fun mapToUiModel(weather: Weather): UiWeather {
        val current1 = weather.current
        val current = UiWeatherCurrent(
            dt = current1.dt,
            sunrise = current1.sunrise.toString().substringBeforeLast(":"),
            sunset = current1.sunset.toString().substringBeforeLast(":"),
            temp = current1.temp.roundToInt(),
            feels_like = "${current1.feels_like.roundToInt()} °",
            pressure = "${current1.pressure} hPa",
            humidity = "${current1.humidity} %",
            uvi = current1.uvi.roundToInt().toString(),
            wind = "${current1.wind.roundToInt()} km/h ${current1.windDirection}",
            rain = "${current1.rain.roundToInt()} mm/24h",
            backgroundImage = current1.backgroundImage
        )

        val weatherDaily = weather.daily.map {
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

        val weatherHourly = weather.hourly.map {
            UiWeatherHourly(
                dt = it.dt,
                temp = it.temp,
                weatherIcon = it.weatherIcon
            )
        }

        return UiWeather(
            current = current,
            daily = weatherDaily,
            hourly = weatherHourly
        )
    }
}

