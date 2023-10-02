package net.dev.weather

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import net.dev.weather.ui.airQuality.AirQualityScreen
import net.dev.weather.ui.currentWeather.CurrentWeatherScreen
import net.dev.weather.ui.places.PlacesScreen
import net.dev.weather.ui.search.SearchScreen
import net.dev.weather.ui.weatherForecast.WeatherForecastScreen

@Composable
fun NavGraph(
    modifier: Modifier = Modifier,
    //startDestination: String = CurrentWeather.route,
    startDestination: String = Places.route,
    navController: NavHostController = rememberNavController()
) {

    NavHost(navController = navController, startDestination = startDestination, modifier = modifier) {
        composable(
            route = CurrentWeather.routeWithArgs,
            arguments = CurrentWeather.arguments,
            deepLinks = CurrentWeather.deepLinks
        ) {
            CurrentWeatherScreen(navController = navController)
        }

        composable(
            route = WeatherForecast.routeWithArgs,
            arguments = WeatherForecast.arguments
        ) {
            WeatherForecastScreen(navController = navController)
        }
        composable(
            route = AirQuality.routeWithArgs,
            arguments = AirQuality.arguments
        ) {
            AirQualityScreen(navController = navController)
        }
        composable(Places.route) {
            PlacesScreen(
                navController = navController,
                onPlaceClick = { placeId -> navController.navigateToCurrentWeather(placeId) })
        }
        composable(Search.route) {
            SearchScreen(navController = navController)
        }
    }
}

fun NavHostController.navigateSingleTopTo(route: String) = this.navigate(route) {
    popUpTo(this@navigateSingleTopTo.graph.findStartDestination().id) {
        saveState = true
    }
    launchSingleTop = true
    restoreState = true
}

private fun NavHostController.navigateToCurrentWeather(placeId: String) {
    this.navigateSingleTopTo("${CurrentWeather.route}/$placeId")
}

private fun NavHostController.navigateToWeatherForecast(placeId: String) {
    this.navigateSingleTopTo("${WeatherForecast.route}/$placeId")
}

private fun NavHostController.navigateToAirQuality(placeId: String) {
    this.navigateSingleTopTo("${AirQuality.route}/$placeId")
}