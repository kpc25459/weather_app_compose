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
    this.navigateSingleTopTo(CurrentWeather.route)
}

private fun NavHostController.navigateToWeatherForecast() {
    this.navigateSingleTopTo(WeatherForecast.route)
}

private fun NavHostController.navigateToAirQuality() {
    this.navigateSingleTopTo(AirQuality.route)
}