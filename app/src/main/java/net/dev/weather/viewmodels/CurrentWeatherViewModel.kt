package net.dev.weather.viewmodels

import androidx.lifecycle.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import net.dev.weather.R
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
                    wind = "${((response.current.wind_speed * 3.6 * 100) / 100).roundToInt()} km/h ${toHumanFromDegrees(response.current.wind_deg)}",
                    rain = "${response.daily.first().rain.roundToInt()} mm/24h",
                    backgroundImage = backgroundImageFromWeather(response.current.weather.first().main)
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
                        rain = it.rain?.`1h` ?: 0.0
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

    private fun toHumanFromDegrees(deg: Int): String {
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
}
