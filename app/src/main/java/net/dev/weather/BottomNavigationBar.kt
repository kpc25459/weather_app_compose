package net.dev.weather

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import net.dev.weather.theme.iconColor

@Composable
fun bottomNavigationBar(navController: NavHostController): @Composable () -> Unit {
    return {
        BottomNavigation(backgroundColor = MaterialTheme.colors.background) {
            val backStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = backStackEntry?.destination

            val items = listOf(
                Screen.CurrentWeather,
                Screen.WeatherForecast,
                Screen.AirQuality,
                Screen.Search
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