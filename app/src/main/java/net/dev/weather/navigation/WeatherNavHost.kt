package net.dev.weather.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import net.dev.weather.ui.WeatherAppState
import net.dev.weather.ui.airQuality.AirQualityScreen
import net.dev.weather.ui.currentWeather.CurrentWeatherScreen
import net.dev.weather.ui.places.PlacesScreen
import net.dev.weather.ui.search.SearchScreen
import net.dev.weather.ui.weatherForecast.WeatherForecastScreen

@Composable
fun WeatherNavHost(
    appState: WeatherAppState,
    modifier: Modifier = Modifier,
    startDestination: String = Places.route,
) {
    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(
            route = CurrentWeather.routeWithArgs,
            arguments = CurrentWeather.arguments,
            deepLinks = CurrentWeather.deepLinks
        ) {
            CurrentWeatherScreen()
        }

        composable(
            route = WeatherForecast.routeWithArgs,
            arguments = WeatherForecast.arguments
        ) {
            WeatherForecastScreen()
        }
        composable(
            route = AirQuality.routeWithArgs,
            arguments = AirQuality.arguments
        ) {
            AirQualityScreen()
        }
        composable(Places.route) {
            PlacesScreen(
                //onPlaceClick = { placeId -> navController.navigateToCurrentWeather(placeId) })
                onPlaceClick = { placeId -> navController.navigateToWeatherForecast(placeId) })
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