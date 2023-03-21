package net.dev.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import net.dev.weather.data.AirPollutionForecastC
import net.dev.weather.data.Weather
import net.dev.weather.data.WeatherRepository
import net.dev.weather.ui.model.*
import kotlin.math.roundToInt

class MainViewModel(weatherRepository: WeatherRepository) : ViewModel() {

    val uiState: StateFlow<MainUiState> = weatherRepository
        .location
        .combine(weatherRepository.weather) { location, weather ->
            val uiWeather = mapToUiModel(weather)
            return@combine Main(
                location = location,
                current = uiWeather.current,
                daily = uiWeather.daily,
                hourlyForecast = uiWeather.hourly
            )
        }
        .combine(weatherRepository.airPollutionForecast) { main, airPollutionForecast ->
            return@combine main.copy(
                airPollutionForecast = mapToUiModel(airPollutionForecast)
            )
        }
        .combine(weatherRepository.airPollutionCurrent) { main, airPollutionCurrent ->
            return@combine main.copy(
                airPollutionCurrent = airPollutionCurrent
            )
        }
        .map(MainUiState::Success)
        .catch { MainUiState.Error(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), MainUiState.Loading)


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

    private fun mapToUiModel(weather: List<AirPollutionForecastC>): List<UiAirPollutionForecast> {
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

sealed interface MainUiState {
    object Loading : MainUiState
    data class Success(val data: Main) : MainUiState
    data class Error(val throwable: Throwable) : MainUiState
}

data class Main(
    val location: String,
    val current: UiWeatherCurrent,
    val daily: List<UiWeatherDaily>,
    val hourlyForecast: List<UiWeatherHourly>,
    val airPollutionCurrent: Int = 0,
    val airPollutionForecast: List<UiAirPollutionForecast> = emptyList()
)

