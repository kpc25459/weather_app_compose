package net.dev.weather

import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink

interface WeatherDestination {
    val route: String
}

object CurrentWeather : WeatherDestination {
    override val route = "current_weather"
    const val placeIdArg = "place_id"

    val routeWithArgs = "$route/{$placeIdArg}"
    val arguments = listOf(
        navArgument(placeIdArg) { type = NavType.StringType }
    )

    val deepLinks = listOf(
        navDeepLink {
            uriPattern = "weather://$route/{$placeIdArg}"
        }
    )
}

object WeatherForecast : WeatherDestination {
    override val route = "weather_forecast"
}

object AirQuality : WeatherDestination {
    override val route = "air_quality"
}

object Places : WeatherDestination {
    override val route = "places"
}

object Search : WeatherDestination {
    override val route = "search"
}

