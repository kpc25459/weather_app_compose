package net.dev.weather

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val route: String,
    @StringRes val titleResourceId: Int,
    val icon: ImageVector
) {
    object CurrentWeather : Screen("current_weather", R.string.current_weather_screen, Icons.Filled.Home)
    object WeatherForecast : Screen("weather_forecast", R.string.weather_Forecast, Icons.Filled.ArrowForward)
    object AirQuality : Screen("air_quality", R.string.air_quality, Icons.Filled.Call)
}
