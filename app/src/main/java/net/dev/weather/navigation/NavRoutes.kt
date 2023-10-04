package net.dev.weather.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptions
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
    const val placeIdArg = "place_id"

    val routeWithArgs = "$route/{$placeIdArg}"
    val arguments = listOf(
        navArgument(placeIdArg) { type = NavType.StringType }
    )
}

object AirQuality : WeatherDestination {
    override val route = "air_quality"

    const val placeIdArg = "place_id"

    val routeWithArgs = "$route/{$placeIdArg}"
    val arguments = listOf(
        navArgument(placeIdArg) { type = NavType.StringType }
    )
}

object Places : WeatherDestination {
    override val route = "places"
}

//TODO: to nie powinno być jako top-level destination
object Search : WeatherDestination {
    override val route = "search"
}

//TODO: tutaj bez nullable?
fun NavController.navigateToCurrentWeather(placeId: String? = null, navOptions: NavOptions? = null) {
    this.navigate(CurrentWeather.routeWithArgs, navOptions)
}

fun NavController.navigateToWeatherForecast(placeId: String? = null, navOptions: NavOptions? = null) {
    this.navigate(WeatherForecast.routeWithArgs, navOptions)
}

fun NavController.navigateToAirQuality(placeId: String? = null, navOptions: NavOptions? = null) {
    this.navigate(AirQuality.routeWithArgs, navOptions)
}

fun NavController.navigateToPlaces(navOptions: NavOptions? = null) {
    this.navigate(Places.route, navOptions)
}

fun NavController.navigateToSearch(navOptions: NavOptions? = null) {
    this.navigate(Search.route, navOptions)
}
