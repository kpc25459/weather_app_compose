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
import net.dev.weather.data.WeatherHourly
import net.dev.weather.data.WeatherRepository

class WeatherForecastViewModel(weatherRepository: WeatherRepository) : ViewModel() {
    private val _cards = MutableStateFlow(listOf<ExpandableCardModel>())
    val cards: StateFlow<List<ExpandableCardModel>> get() = _cards

    private val _expandedCardIdsList = MutableStateFlow(listOf<Int>())
    val expandedCardIdsList: StateFlow<List<Int>> get() = _expandedCardIdsList


    init {

        viewModelScope.launch {

            //TODO: tutaj jakoś bezpośrednio i może take(24) na końcu
            weatherRepository.getWeather().collectLatest { response ->

                val timeZone = TimeZone.of(response.timezone)

                val models = response.hourly.take(24).map {
                    ExpandableCardModel(
                        it.dt,
                        WeatherHourly(
                            Instant.fromEpochSeconds(it.dt.toLong()).toLocalDateTime(timeZone),
                            it.sunrise,
                            it.sunset,
                            it.temp,
                            it.feels_like,
                            it.pressure,
                            it.humidity,
                            it.dew_point,
                            it.uvi,
                            it.clouds,
                            it.visibility,
                            it.wind_speed,
                            it.wind_deg,
                            it.wind_gust,
                            it.weather,
                            it.pop,
                            rain = it.rain?.`1h` ?: 0.0
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
