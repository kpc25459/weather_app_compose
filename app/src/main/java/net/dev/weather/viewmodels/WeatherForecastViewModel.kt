package net.dev.weather.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import net.dev.weather.data.ExpandableCardModel
import net.dev.weather.data.WeatherDaily
import net.dev.weather.data.WeatherRepository
import net.dev.weather.toHumanFromDegrees
import kotlin.math.roundToInt

class WeatherForecastViewModel(weatherRepository: WeatherRepository) : ViewModel() {
    private val _cards = MutableStateFlow(listOf<ExpandableCardModel>())
    val cards: StateFlow<List<ExpandableCardModel>> get() = _cards

    private val _expandedCardIdsList = MutableStateFlow(listOf<Int>())
    val expandedCardIdsList: StateFlow<List<Int>> get() = _expandedCardIdsList


    init {

        viewModelScope.launch {

            weatherRepository.getWeather().collectLatest { response ->

                val timeZone = TimeZone.of(response.timezone)

                val models = response.daily.subList(1, 8).map {
                    ExpandableCardModel(
                        it.dt,
                        WeatherDaily(
                            Instant.fromEpochSeconds(it.dt.toLong()).toLocalDateTime(timeZone),
                            description = it.weather.first().description,
                            sunrise = Instant.fromEpochSeconds(it.sunrise.toLong()).toLocalDateTime(timeZone).time.toString().substringBeforeLast(":"),
                            sunset = Instant.fromEpochSeconds(it.sunset.toLong()).toLocalDateTime(timeZone).time.toString().substringBeforeLast(":"),
                            temp = "${it.temp.day}°C / ${it.temp.night}°C",
                            pressure = "${it.pressure} hPa",
                            humidity = "${it.humidity} %",
                            wind = "${((it.wind_speed * 3.6 * 100) / 100).roundToInt()} km/h ${toHumanFromDegrees(it.wind_deg)}",
                            rain = "${it.rain.roundToInt()} mm/24h",
                            uvi = it.uvi.roundToInt().toString(),
                        )
                    )
                }
                _cards.emit(models)
            }
        }
    }

    fun onCardArrowClicked(dt: Int) {
        _expandedCardIdsList.value = _expandedCardIdsList.value.toMutableList().also {
            if (it.contains(dt)) it.remove(dt) else it.add(dt)
        }
    }
}