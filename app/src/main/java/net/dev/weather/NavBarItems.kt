package net.dev.weather

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

data class BarItem(
    val title: String,
    val icon: ImageVector,
    val route: String
)

object NavBarItems {

    val BarItems = listOf(
        //BarItem("Current Weather", Icons.CurrentWeather, NavRoutes.CurrentWeather.route),
        BarItem("Current weather", Icons.Filled.Home, "current_weather"),
        BarItem("Weather forecast", Icons.Filled.ArrowForward, "weather_forecast"),
        BarItem("Air quality", Icons.Filled.Call, "air_quality")
    )
}