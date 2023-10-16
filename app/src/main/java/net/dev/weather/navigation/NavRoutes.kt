package net.dev.weather.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.navDeepLink

interface WeatherDestination {
    val route: String
}

object CurrentWeather : WeatherDestination {
    override val route = "current_weather"

    val deepLinks = listOf(
        navDeepLink {
            uriPattern = "weather://$route"
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

fun NavController.navigateToCurrentWeather(navOptions: NavOptions? = null) {
    this.navigate(CurrentWeather.route, navOptions)
}

fun NavController.navigateToWeatherForecast(navOptions: NavOptions? = null) {
    this.navigate(WeatherForecast.route, navOptions)
}

fun NavController.navigateToAirQuality(navOptions: NavOptions? = null) {
    this.navigate(AirQuality.route, navOptions)
}

fun NavController.navigateToPlaces(navOptions: NavOptions? = null) {
    this.navigate(Places.route, navOptions)
}

fun NavController.navigateToSearch(navOptions: NavOptions? = null) {
    this.navigate(Search.route, navOptions)
}
