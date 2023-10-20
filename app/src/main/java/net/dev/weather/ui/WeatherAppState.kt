package net.dev.weather.ui

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import net.dev.weather.navigation.AirQuality
import net.dev.weather.navigation.CurrentWeather
import net.dev.weather.navigation.Places
import net.dev.weather.navigation.TopLevelDestination
import net.dev.weather.navigation.WeatherForecast
import net.dev.weather.navigation.navigateToAirQuality
import net.dev.weather.navigation.navigateToCurrentWeather
import net.dev.weather.navigation.navigateToPlaces
import net.dev.weather.navigation.navigateToSearch
import net.dev.weather.navigation.navigateToWeatherForecast

@Composable
fun rememberWeatherAppState(
    windowSizeClass: WindowSizeClass,
    navController: NavHostController = rememberNavController(),
): WeatherAppState {
    return remember(navController, windowSizeClass) {
        WeatherAppState(navController, windowSizeClass)
    }
}

class WeatherAppState(
    val navController: NavHostController,
    val windowSizeClass: WindowSizeClass
) {

    val shouldShowBottomBar: Boolean
        get() = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact

    val shouldShowNavRail: Boolean
        get() = !shouldShowBottomBar

    val currentDestination: NavDestination?
        @Composable get() = navController.currentBackStackEntryAsState().value?.destination

    val currentTopLevelDestination: TopLevelDestination?
        @Composable get() = when (currentDestination?.route) {
            CurrentWeather.route -> TopLevelDestination.CURRENT_WEATHER
            WeatherForecast.route -> TopLevelDestination.WEATHER_FORECAST
            AirQuality.route -> TopLevelDestination.AIR_QUALITY
            Places.route -> TopLevelDestination.PLACES
            else -> null
        }

    val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.values().asList()

    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {

        val topLevelNavOptions = navOptions {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }

            // avoid multiple copies of the same destination on the back stack
            launchSingleTop = true

            // restore state when reselecting a previously selected item
            restoreState = true
        }

        when (topLevelDestination) {
            TopLevelDestination.CURRENT_WEATHER -> navController.navigateToCurrentWeather(navOptions = topLevelNavOptions)
            TopLevelDestination.WEATHER_FORECAST -> navController.navigateToWeatherForecast(navOptions = topLevelNavOptions)
            TopLevelDestination.AIR_QUALITY -> navController.navigateToAirQuality(navOptions = topLevelNavOptions)
            TopLevelDestination.PLACES -> navController.navigateToPlaces(navOptions = topLevelNavOptions)
        }
    }

    fun navigateToSearch() {
        navController.navigateToSearch()
    }
}