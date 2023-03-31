package net.dev.weather

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun WeatherApp() {
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
        NavGraph()

        //MainPage()
    }
}
/*
@Composable
fun MainPage() {

    val navController = rememberNavController()

    Scaffold(
        topBar = topBar(navController = navController),
        bottomBar = bottomNavigationBar(navController = navController)
    )
    { innerPadding ->
        NavigationHost(navController = navController, innerPadding)
    }
}

@Composable
private fun NavigationHost(navController: NavHostController, innerPadding: PaddingValues) {
    NavHost(navController = navController, startDestination = NavRoutes.CurrentWeather.route, Modifier.padding(innerPadding)) {
        composable(NavRoutes.CurrentWeather.route) {
            CurrentWeatherScreen(navController)
        }
        composable(NavRoutes.WeatherForecast.route) {
            WeatherForecastScreen(navController)
        }
        composable(NavRoutes.AirQuality.route) {
            AirQualityScreen(navController)
        }
        composable(NavRoutes.Search.route) {
            SearchScreen(navController)
        }
    }
}*/

/*
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    WeatherTheme {
        MainPage(uiState.data)
    }
}
*/
