package net.dev.weather.viewmodels

import androidx.lifecycle.MutableLiveData

class CurrentWeatherViewModel {

    val date = MutableLiveData<String>()

    val time = MutableLiveData<String>()

    val location = MutableLiveData<String>()
    val temperature = MutableLiveData<String>()

    val airQuality = MutableLiveData<String>()

    init {
        load()
    }

    fun load() {

        date.value = "2021-05-20"
        time.value = "12:00"
        location.value = "Warszawa"
        temperature.value = "6°"
        airQuality.value = "Dobra"
    }

    fun load2() {

        date.value = "2021-05-21"
        time.value = "13:00"
        location.value = "Poznań"
        temperature.value = "7°"
        airQuality.value = "Bardzo Dobra"
    }
}