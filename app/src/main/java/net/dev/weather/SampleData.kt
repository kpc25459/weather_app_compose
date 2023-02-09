package net.dev.weather

import kotlinx.datetime.LocalDateTime
import net.dev.weather.data.AirPollutionForecast
import net.dev.weather.data.WeatherCurrent

val sampleMain = Main(
    location = "Warsaw",
    current = WeatherCurrent(
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
    airQuality = 0,
    airPollutionForecast = listOf(
        AirPollutionForecast(
            dt = LocalDateTime(2021, 5, 1, 10, 0),
            co = 607.49,
            no = 14.53,
            no2 = 23.31,
            o3 = 0.08,
            so2 = 23.84,
            pm2_5 = 40.52,
            pm10 = 49.45,
            nh3 = 1.5,
            aqi = 4
        ),
        AirPollutionForecast(
            dt = LocalDateTime(2021, 5, 1, 11, 0),
            co = 594.14,
            no = 12.41,
            no2 = 23.99,
            o3 = 0.13,
            so2 = 21.7,
            pm2_5 = 39.76,
            pm10 = 47.98,
            nh3 = 1.38,
            aqi = 2
        )
    )
)
