package net.dev.weather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import net.dev.weather.screens.AirQualityScreen
import net.dev.weather.screens.CurrentWeatherScreen
import net.dev.weather.ui.weatherForecast.WeatherForecastScreen
import net.dev.weather.theme.WeatherTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    MainPage()
                }
            }
        }
    }
}

@Composable
fun MainPage() {

    val navController = rememberNavController()

    val currentTabRoute = navController.currentBackStackEntryAsState().value?.destination?.hierarchy?.firstOrNull()?.route

    Scaffold(
        topBar = topBar(currentTabRoute),
        bottomBar = bottomNavigationBar(navController = navController)
    )
    { innerPadding ->
        NavigationHost(navController = navController, innerPadding)
    }
}

@Composable
private fun topBar(currentTab: String?): @Composable () -> Unit {
    if (currentTab != NavRoutes.CurrentWeather.route) {
        return { TopAppBar(title = { Text(text = "Weather") }) }
    } else {
        return {}
    }
}

@Composable
private fun bottomNavigationBar(navController: NavHostController): @Composable () -> Unit {
    return {
        BottomNavigation {
            val backStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = backStackEntry?.destination

            val items = listOf(
                Screen.CurrentWeather,
                Screen.WeatherForecast,
                Screen.AirQuality
            )

            items.forEach { screen ->
                BottomNavigationItem(
                    icon = { Icon(screen.icon, contentDescription = screen.title) },
                    label = { Text(screen.title) },
                    selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                    onClick = {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    }
}


@Composable
private fun NavigationHost(navController: NavHostController, innerPadding: PaddingValues) {
    NavHost(navController = navController, startDestination = NavRoutes.CurrentWeather.route, Modifier.padding(innerPadding)) {
        composable(NavRoutes.CurrentWeather.route) {
            CurrentWeatherScreen()
        }
        composable(NavRoutes.WeatherForecast.route) {
            WeatherForecastScreen()
        }
        composable(NavRoutes.AirQuality.route) {
            AirQualityScreen()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    WeatherTheme {
        MainPage()
    }
}