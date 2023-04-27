package net.dev.weather

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
    startDestination: String = NavRoutes.CurrentWeather.route,
    navController: NavHostController = rememberNavController()
) {

    NavHost(navController = navController, startDestination = startDestination, modifier = modifier) {
        composable(NavRoutes.CurrentWeather.route) {
            CurrentWeatherScreen(navController = navController)
        }
        composable(NavRoutes.WeatherForecast.route) {
            WeatherForecastScreen(navController = navController)
        }
        composable(NavRoutes.AirQuality.route) {
            AirQualityScreen(navController = navController)
        }
        composable(NavRoutes.Places.route) {
            PlacesScreen(navController = navController)
        }
    }


}