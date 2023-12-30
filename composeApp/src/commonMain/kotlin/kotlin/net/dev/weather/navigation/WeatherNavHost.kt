package net.dev.weather.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import net.dev.weather.ui.WeatherAppState
import kotlin.net.dev.weather.ui.currentWeather.CurrentWeatherScreen
import kotlin.net.dev.weather.ui.places.PlacesScreen
import kotlin.net.dev.weather.ui.search.SearchScreen
import net.dev.weather.ui.weatherForecast.WeatherForecastScreen
import kotlin.net.dev.weather.navigation.AirQuality
import kotlin.net.dev.weather.navigation.CurrentWeather
import kotlin.net.dev.weather.navigation.Places
import kotlin.net.dev.weather.navigation.Search
import kotlin.net.dev.weather.navigation.WeatherForecast
import kotlin.net.dev.weather.ui.airQuality.AirQualityScreen

@Composable
fun WeatherNavHost(
    appState: WeatherAppState,
    modifier: Modifier = Modifier,
    startDestination: String = CurrentWeather.route,
) {
    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(
            route = CurrentWeather.route,
            deepLinks = CurrentWeather.deepLinks
        ) {
            CurrentWeatherScreen()
        }

        composable(
            route = WeatherForecast.route,
        ) {
            WeatherForecastScreen()
        }
        composable(
            route = AirQuality.route,
        ) {
            AirQualityScreen()
        }
        composable(Places.route) {
            PlacesScreen(onPlaceClick = { _ ->
                navController.navigateToCurrentWeather()
            })
        }
        composable(Search.route) {
            SearchScreen(
                onSuggestionClick = { _ ->
                    navController.popBackStack()
                    navController.navigateToCurrentWeather()
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
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

private fun NavHostController.navigateToCurrentWeather() {
    this.navigateSingleTopTo(kotlin.net.dev.weather.navigation.CurrentWeather.route)
}

private fun NavHostController.navigateToWeatherForecast() {
    this.navigateSingleTopTo(kotlin.net.dev.weather.navigation.WeatherForecast.route)
}

private fun NavHostController.navigateToAirQuality() {
    this.navigateSingleTopTo(kotlin.net.dev.weather.navigation.AirQuality.route)
}