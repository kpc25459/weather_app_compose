package net.dev.weather.viewmodels

import androidx.lifecycle.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import net.dev.weather.data.CurrentWeather
import net.dev.weather.data.WeatherDaily
import net.dev.weather.data.WeatherHourly
import net.dev.weather.data.WeatherRepository
import kotlin.math.roundToInt

class CurrentWeatherViewModel(weatherRepository: WeatherRepository) : ViewModel() {

    val location = MutableLiveData<String>()

    val airQuality = MutableLiveData<String>()

    val currentWeather: MutableLiveData<CurrentWeather> = MutableLiveData()
    val hourlyForecast: MutableLiveData<List<WeatherHourly>> = MutableLiveData()
    val dailyForecast: MutableLiveData<List<WeatherDaily>> = MutableLiveData()

    init {

        viewModelScope.launch {

            weatherRepository.getLocation().collectLatest { location.value = it }

            weatherRepository.getWeather().collectLatest { response ->

                val timeZone = TimeZone.of(response.timezone)

                currentWeather.value = CurrentWeather(
                    Instant.fromEpochSeconds(response.current.dt.toLong()).toLocalDateTime(timeZone),
                    Instant.fromEpochSeconds(response.current.sunrise.toLong()).toLocalDateTime(timeZone).time.toString().substringBeforeLast(":"),
                    Instant.fromEpochSeconds(response.current.sunset.toLong()).toLocalDateTime(timeZone).time.toString().substringBeforeLast(":"),
                    response.current.temp.roundToInt(),
                    feels_like = "${response.current.feels_like.roundToInt()} °",
                    pressure = "${response.current.pressure} hPa",
                    humidity = "${response.current.humidity} %",
                    response.current.dew_point,
                    response.current.uvi.roundToInt().toString(),
                    response.current.clouds,
                    response.current.visibility,
                    wind = "${((response.current.wind_speed * 3.6 * 100) / 100).roundToInt()} km/h ${toHuman(response.current.wind_deg)}",
                    rain = "${response.daily.first().rain.roundToInt()} mm/24h"
                )

                hourlyForecast.value = response.hourly.take(24).map {
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
                        it.rain
                    )
                }

                dailyForecast.value = response.daily.take(7).map {
                    WeatherDaily(
                        Instant.fromEpochSeconds(it.dt.toLong()).toLocalDateTime(timeZone),
                        it.sunrise,
                        it.sunset,
                        it.moonrise,
                        it.moonset,
                        it.moon_phase,
                        it.temp.day,
                        it.feels_like.day,
                        it.pressure,
                        it.humidity,
                        it.dew_point,
                        it.wind_speed,
                        it.wind_deg,
                        it.wind_gust,
                        //it.weather,
                        it.clouds,
                        it.pop,
                        it.rain,
                        it.uvi
                    )
                }
            }

            weatherRepository.getAirQuality().collectLatest { airQuality.value = fromAqiIndex(it) }
        }
    }
}

private fun fromAqiIndex(aqi: Int): String {
    return when (aqi) {
        1 -> "Bardzo dobra"
        2 -> "Dobra"
        3 -> "Dostateczna"
        4 -> "Zła"
        5 -> "Bardzo zła"
        else -> "Nieznana"
    }
}

private fun toHuman(deg: Int): String {
    if (deg > 337.5) {
        return "N";
    } else if (deg > 292.5) {
        return "NW";
    } else if (deg > 247.5) {
        return "W";
    } else if (deg > 202.5) {
        return "SW";
    } else if (deg > 157.5) {
        return "S";
    } else if (deg > 122.5) {
        return "SE";
    } else if (deg > 67.5) {
        return "E";
    } else if (deg > 22.5) {
        return "NE";
    } else {
        return "";
    }
}