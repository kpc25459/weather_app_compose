package net.dev.weather.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import net.dev.weather.api.LocationServiceApi
import net.dev.weather.api.WeatherHourly

data class HourlyForecast(val hour: String, val temperature: String)

class CurrentWeatherViewModel : ViewModel() {

    val locationServiceApi = LocationServiceApi.create()


    val date = MutableLiveData<String>()

    val time = MutableLiveData<String>()

    val location = MutableLiveData<String>()
    val temperature = MutableLiveData<String>()

    val airQuality = MutableLiveData<String>()

    val timeZone: MutableLiveData<TimeZone> = MutableLiveData()
    val hourlyForecast: MutableLiveData<List<WeatherHourly>> = MutableLiveData()


    init {
        viewModelScope.launch {
            val locationResult = locationServiceApi.getReverseLocation()
            location.value = locationResult.body()?.first()?.name

            val response = locationServiceApi.getHourlyForecast().body() ?: throw Exception("No data")

            timeZone.value = TimeZone.of(response.timezone)

            hourlyForecast.value = response.hourly
            date.value = "2021-05-20"
            time.value = "12:00"
            temperature.value = "6°"
            airQuality.value = "Dobra"
        }
    }
}