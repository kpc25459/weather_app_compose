package net.dev.weather

import kotlinx.datetime.LocalDateTime
import net.dev.weather.data.Main
import net.dev.weather.ui.model.UiAirPollutionForecast
import net.dev.weather.ui.model.UiWeatherCurrent

val sampleMain = Main(
    location = "Warsaw",
    current = UiWeatherCurrent(
        dt = LocalDateTime(2021, 5, 1, 10, 0),
        sunrise = "10:00",
        sunset = "22:00",
        temp = 2,
        feels_like = "1",
        pressure = "1000",
        uvi = "1",
        wind = "1",
        rain = "1",
        backgroundImage = R.drawable.ic_launcher_background,
        humidity = "1"
    ),
    daily = listOf(),
    hourlyForecast = listOf(),
    airPollutionCurrent = 0,
    airPollutionForecast = listOf(
        UiAirPollutionForecast(
            dt = "10:00",
            no2 = "23.31",
            o3 = "0.08",
            pm2_5 = 40.52,
            pm2_5String = "40.52",
            pm10 = 49.45,
            pm10String = "49.45",
            aqi = 4
        ),
        UiAirPollutionForecast(
            dt = "11:00",
            no2 = "23.99",
            o3 = "0.13",
            pm2_5 = 39.76,
            pm2_5String = "39.76",
            pm10 = 47.98,
            pm10String = "47.98",
            aqi = 2
        )
    )
)
