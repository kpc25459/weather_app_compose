package net.dev.weather.ui.currentWeather

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import net.dev.weather.R
import net.dev.weather.data.*
import kotlin.math.roundToInt

class CurrentWeatherViewModel(weatherRepository: WeatherRepository) : ViewModel() {

    val uiState: StateFlow<CurrentWeatherUiState> = weatherRepository
        .currentWeather
        .take(7)
        .combine(weatherRepository.currentWeather) { daily, current ->
            currentWeatherData
        }
        .combine(weatherRepository.location) { currentWeatherData, location ->
            Success(currentWeatherData, location)
        }
        .map(CurrentWeatherUiState::Success)
        .catch { CurrentWeatherUiState.Error(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), CurrentWeatherUiState.Loading)

    val currentWeather: MutableLiveData<CurrentWeather> = MutableLiveData()
    val hourlyForecast: MutableLiveData<List<WeatherHourly>> = MutableLiveData()
    val airQuality = MutableLiveData<String>()

    init {

        viewModelScope.launch {

            /* weatherRepository.getLocation().collectLatest { location.value = it }

             weatherRepository.getWeather().collectLatest { response ->

                 val timeZone = TimeZone.of(response.timezone)

                 currentWeather.value = CurrentWeather(
                     dt = Instant.fromEpochSeconds(response.current.dt.toLong()).toLocalDateTime(timeZone),
                     sunrise = Instant.fromEpochSeconds(response.current.sunrise.toLong()).toLocalDateTime(timeZone).time.toString().substringBeforeLast(":"),
                     sunset = Instant.fromEpochSeconds(response.current.sunset.toLong()).toLocalDateTime(timeZone).time.toString().substringBeforeLast(":"),
                     temp = response.current.temp.roundToInt(),
                     feels_like = "${response.current.feels_like.roundToInt()} °",
                     pressure = "${response.current.pressure} hPa",
                     humidity = "${response.current.humidity} %",
                     dew_point = response.current.dew_point,
                     uvi = response.current.uvi.roundToInt().toString(),
                     clouds = response.current.clouds,
                     visibility = response.current.visibility,
                     wind = "${((response.current.wind_speed * 3.6 * 100) / 100).roundToInt()} km/h ${toHumanFromDegrees(response.current.wind_deg)}",
                     rain = "${response.daily.first().rain.roundToInt()} mm/24h",
                     backgroundImage = backgroundImageFromWeather(response.current.weather.first().main)
                 )

                 hourlyForecast.value = response.hourly.take(24).map {
                     WeatherHourly(
                         dt = Instant.fromEpochSeconds(it.dt.toLong()).toLocalDateTime(timeZone),
                         sunrise = it.sunrise,
                         sunset = it.sunset,
                         temp = it.temp,
                         feels_like = it.feels_like,
                         pressure = it.pressure,
                         humidity = it.humidity,
                         dew_point = it.dew_point,
                         uvi = it.uvi,
                         clouds = it.clouds,
                         visibility = it.visibility,
                         wind_speed = it.wind_speed,
                         wind_deg = it.wind_deg,
                         wind_gust = it.wind_gust,
                         weather = it.weather,
                         pop = it.pop,
                         rain = it.rain?.`1h` ?: 0.0
                     )
                 }*/

        }

        /*           weatherRepository.getAirQuality().collectLatest { airQuality.value = fromAqiIndex(it) }*/
/*        }*/
    }





}

sealed interface CurrentWeatherUiState {
    object Loading : CurrentWeatherUiState
    data class Success(val data: MainWeather) : CurrentWeatherUiState
    data class Error(val throwable: Throwable) : CurrentWeatherUiState
}

data class MainWeather(val location: String, val currentWeather: CurrentWeather, val hourlyForecast: List<WeatherHourly>, val airQuality: String)