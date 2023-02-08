package net.dev.weather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import net.dev.weather.theme.*
import net.dev.weather.ui.currentWeather.CurrentWeatherScreen
import net.dev.weather.ui.weatherForecast.WeatherForecastScreen
import net.dev.weather.ui.airQuality.AirQualityScreen

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

    Scaffold(
        topBar = topBar(navController = navController),
        bottomBar = bottomNavigationBar(navController = navController)
    )
    { innerPadding ->
        NavigationHost(navController = navController, innerPadding)
    }
}

@Composable
private fun topBar(navController: NavHostController/*, title: String*/): @Composable () -> Unit {

    val currentTab = navController.currentBackStackEntryAsState().value?.destination?.hierarchy?.firstOrNull() ?: return {}

    if (currentTab.route != NavRoutes.CurrentWeather.route) {
        return {
            TopAppBar(
                title = { Text(text = getTitleByRoute(route = currentTab.route!!)) },
                backgroundColor = tabBarBackgroundColor,
                contentColor = tabBarTextColor,
                elevation = 0.dp
            )
        }
    } else {
        return {}
    }
}

@Composable
private fun bottomNavigationBar(navController: NavHostController): @Composable () -> Unit {
    return {
        BottomNavigation(backgroundColor = MaterialTheme.colors.background) {
            val backStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = backStackEntry?.destination

            val items = listOf(
                Screen.CurrentWeather,
                Screen.WeatherForecast,
                Screen.AirQuality
            )

            items.forEach { screen ->
                val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true

                BottomNavigationItem(
                    icon = { Icon(painter = painterResource(screen.iconResourceId), contentDescription = stringResource(screen.titleResourceId), tint = if (selected) iconColor else Color.Black) },
                    selected = selected,
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
