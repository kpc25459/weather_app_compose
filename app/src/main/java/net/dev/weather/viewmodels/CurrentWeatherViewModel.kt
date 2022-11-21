package net.dev.weather.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.dev.weather.api.LocationServiceApi

class CurrentWeatherViewModel : ViewModel() {
    val date = MutableLiveData<String>()

    val time = MutableLiveData<String>()

    val location = MutableLiveData<String>()
    val temperature = MutableLiveData<String>()

    val airQuality = MutableLiveData<String>()

    val quotesApi = LocationServiceApi.create()

    init {
        viewModelScope.launch {
            val result = quotesApi.getReverseLocation("52.335833", "16.807778")
            val name = result.body()?.first()?.name

            location.value = name

            date.value = "2021-05-20"
            time.value = "12:00"
            temperature.value = "6°"
            airQuality.value = "Dobra"
        }
    }
}