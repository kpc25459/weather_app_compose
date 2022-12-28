package net.dev.weather

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val route: String,
    //TOOD: tutaj na resoursy
    //@StringRes val resourceId: Int,
    val title: String,
    val icon: ImageVector
) {
    object CurrentWeather : Screen("current_weather", "Current weather", Icons.Filled.Home)
    object WeatherForecast : Screen("weather_forecast", "Weather forecast", Icons.Filled.ArrowForward)
    object AirQuality : Screen("air_quality", "Air quality", Icons.Filled.Call)
}