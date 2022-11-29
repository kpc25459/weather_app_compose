package net.dev.weather.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import net.dev.weather.api.WeatherDaily
import net.dev.weather.api.WeatherServiceApi
import net.dev.weather.api.WeatherHourly

data class HourlyForecast(val hour: String, val temperature: String)

class CurrentWeatherViewModel : ViewModel() {

    val locationServiceApi = WeatherServiceApi.create()

    val location = MutableLiveData<String>()

    val airQuality = MutableLiveData<String>()

    val timeZone: MutableLiveData<TimeZone> = MutableLiveData()

    val currentWeather: MutableLiveData<WeatherHourly> = MutableLiveData()
    val hourlyForecast: MutableLiveData<List<WeatherHourly>> = MutableLiveData()
    val dailyForecast: MutableLiveData<List<WeatherDaily>> = MutableLiveData()


    init {
        viewModelScope.launch {
            val reverseLocationResponse = locationServiceApi.getReverseLocation()
            location.value = reverseLocationResponse.body()?.first()?.name

            val getWeatherResponse = locationServiceApi.getWeather().body() ?: throw Exception("No data")
            timeZone.value = TimeZone.of(getWeatherResponse.timezone)
            currentWeather.value = getWeatherResponse.current
            hourlyForecast.value = getWeatherResponse.hourly.take(24)
            dailyForecast.value = getWeatherResponse.daily.takeLast(7)

            val airPollutionResponse = locationServiceApi.getAirPollution().body() ?: throw Exception("No data")
            airQuality.value = fromAqiIndex(airPollutionResponse.list.first().main.aqi)
        }
    }

    private fun fromAqiIndex(aqi: Int): String {
        when (aqi) {
            1 -> return "Bardzo dobra"
            2 -> return "Dobra"
            3 -> return "Dostateczna"
            4 -> return "Zła"
            5 -> return "Bardzo zła"
            else -> return "Nieznana"
        }
    }
}