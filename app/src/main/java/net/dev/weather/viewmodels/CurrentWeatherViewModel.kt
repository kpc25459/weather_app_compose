package net.dev.weather.viewmodels

import androidx.lifecycle.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import net.dev.weather.R
import net.dev.weather.data.*
import net.dev.weather.fromAqiIndex
import net.dev.weather.toHumanFromDegrees
import kotlin.math.roundToInt

class CurrentWeatherViewModel(weatherRepository: WeatherRepository) : ViewModel() {

    val location = MutableLiveData<String>()

    val airQuality = MutableLiveData<String>()

    val currentWeather: MutableLiveData<CurrentWeather> = MutableLiveData()
    val hourlyForecast: MutableLiveData<List<WeatherHourly>> = MutableLiveData()
    //val dailyForecast: MutableLiveData<List<WeatherDaily>> = MutableLiveData()

    init {

        viewModelScope.launch {

            weatherRepository.getLocation().collectLatest { location.value = it }

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
                }

              /*  dailyForecast.value = response.daily.take(7).map {
                    WeatherDaily(
                        Instant.fromEpochSeconds(it.dt.toLong()).toLocalDateTime(timeZone),
                        *//*sunrise = it.sunrise,
                        sunset = it.sunset,
                        moonrise = it.moonrise,
                        moonset = it.moonset,
                        moon_phase = it.moon_phase,
                        temp = it.temp.day,
                        feels_like = it.feels_like.day,
                        pressure = it.pressure,
                        humidity = it.humidity,
                        dew_point = it.dew_point,
                        description = it.weather.first().description,
                        clouds = it.clouds,
                        pop = it.pop,
                        rain = it.rain,
                        uvi = it.uvi*//*
                    )
                }*/
            }

            weatherRepository.getAirQuality().collectLatest { airQuality.value = fromAqiIndex(it) }
        }
    }


    private fun backgroundImageFromWeather(input: String): Int {
        return when (weatherCondition(input)) {
            WeatherCondition.Thunderstorm -> R.drawable.new_thunder
            WeatherCondition.Drizzle -> R.drawable.new_rain
            WeatherCondition.Rain -> R.drawable.new_rain
            WeatherCondition.Snow -> R.drawable.new_snow
            WeatherCondition.Fog -> R.drawable.new_fog
            WeatherCondition.Clear -> R.drawable.clear_new
            WeatherCondition.Clouds -> R.drawable.clouds_new
            WeatherCondition.Unknown -> R.drawable.unknown
        }
    }

    private fun weatherCondition(input: String) = when (input) {
        "Thunderstorm" -> WeatherCondition.Thunderstorm
        "Drizzle" -> WeatherCondition.Drizzle
        "Rain" -> WeatherCondition.Rain
        "Snow" -> WeatherCondition.Snow
        "Mist", "Fog", "Smoke", "Haze", "Dust", "Sand", "Ash", "Squall", "Tornado" -> WeatherCondition.Fog
        "Clear" -> WeatherCondition.Clear
        "Clouds" -> WeatherCondition.Clouds
        else -> WeatherCondition.Unknown
    }

    enum class WeatherCondition {
        Thunderstorm, Drizzle, Rain, Snow, Fog, Clear, Clouds, Unknown
    }
}
