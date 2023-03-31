package net.dev.weather

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import net.dev.weather.data.AirPollutionForecast
import net.dev.weather.data.Main
import net.dev.weather.data.Weather
import net.dev.weather.data.WeatherRepository
import net.dev.weather.ui.model.*
import net.dev.weather.utils.Async
import kotlin.math.roundToInt

data class MainUiState(
    val main: Main? = null,
    val isLoading: Boolean = false,
    @StringRes val userMessage: Int? = null
)

class MainViewModel(weatherRepository: WeatherRepository) : ViewModel() {

    private val _userMessage: MutableStateFlow<Int?> = MutableStateFlow(null)

    private val _data: Flow<Async<Main>> =
        combine(
            weatherRepository.location,
            weatherRepository.weather,
            weatherRepository.airPollutionForecast,
            weatherRepository.airPollutionCurrent
        ) { location, weather, airPollutionForecast, airPollutionCurrent ->

            val uiWeather = mapToUiModel(weather)
            return@combine Main(
                location = location,
                current = uiWeather.current,
                daily = uiWeather.daily,
                hourlyForecast = uiWeather.hourly,
                airPollutionForecast = mapToUiModel(airPollutionForecast),
                airPollutionCurrent = airPollutionCurrent
            )
        }
            .map { Async.Success(it) }
            .catch<Async<Main>> { emit(Async.Error(R.string.loading_error, it)) }

    val uiState: StateFlow<MainUiState> = combine(
        _data, _userMessage
    ) { data, userMessage ->
        when (data) {
            is Async.Loading -> MainUiState(isLoading = true)
            is Async.Error -> MainUiState(userMessage = data.message)
            is Async.Success -> MainUiState(main = data.data, userMessage = userMessage)
        }
    }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), MainUiState(isLoading = true))


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

