package kotlin.net.dev.weather.data.model

import kotlinx.datetime.LocalDateTime

data class AirPollutionForecast(
    val dt: LocalDateTime,
    val aqi: Int,
    val no2: Double,
    val o3: Double,
    val pm2_5: Double,
    val pm10: Double,
)

val airPollutionForecastPreviewItems = listOf(
    AirPollutionForecast(
        dt = LocalDateTime(2022, 2, 2, 12, 23),
        no2 = 23.31,
        o3 = 0.08,
        pm2_5 = 40.52,
        pm10 = 49.45,
        aqi = 2
    ),
    AirPollutionForecast(
        dt = LocalDateTime(2022, 2, 2, 10, 0),
        no2 = 23.99,
        o3 = 0.13,
        pm2_5 = 39.76,
        pm10 = 47.98,
        aqi = 4
    )
)