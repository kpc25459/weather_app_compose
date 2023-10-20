package net.dev.weather.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import net.dev.weather.components.WeatherNavigationRail
import net.dev.weather.components.WeatherTopAppBar
import net.dev.weather.navigation.TopLevelDestination
import net.dev.weather.navigation.WeatherNavHost

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherApp(
    windowSizeClass: WindowSizeClass,
    appState: WeatherAppState = rememberWeatherAppState(
        windowSizeClass = windowSizeClass
    ),
) {
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {

        Scaffold(
            bottomBar = {
                if (appState.shouldShowBottomBar) {
                    WeatherBottomBar(
                        destinations = appState.topLevelDestinations,
                        onNavigateTopLevelDestination = appState::navigateToTopLevelDestination,
                        currentDestination = appState.currentDestination
                    )
                }
            },
        ) { paddingValues ->

            Row(
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                if (appState.shouldShowNavRail) {
                    WeatherNavRail(
                        destinations = appState.topLevelDestinations,
                        onNavigateTopLevelDestination = appState::navigateToTopLevelDestination,
                        currentDestination = appState.currentDestination
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {

                    val destination = appState.currentTopLevelDestination
                    if (destination != null) {
                        WeatherTopAppBar(
                            currentDestination = destination,
                            onSearchClick = {
                                appState.navigateToSearch()
                            })
                    }

                    WeatherNavHost(appState = appState)
                }
            }
        }
    }
}

@Composable
fun WeatherBottomBar(
    destinations: List<TopLevelDestination>,
    onNavigateTopLevelDestination: (TopLevelDestination) -> Unit,
    currentDestination: NavDestination?,
    modifier: Modifier = Modifier
) {
    NavigationBar {

        destinations.forEach { destination ->

            val selected = currentDestination?.isTopLevelDestination(destination)

            //TODO: custom navigationbaritem
            NavigationBarItem(
                icon = {
                    Icon(imageVector = ImageVector.vectorResource(destination.icon), contentDescription = null)
                },
                selected = selected ?: false,
                onClick = { onNavigateTopLevelDestination(destination) },
                modifier = modifier
            )
        }
    }

}

@Composable
fun WeatherNavRail(destinations: List<TopLevelDestination>, onNavigateTopLevelDestination: (TopLevelDestination) -> Unit, currentDestination: NavDestination?, modifier: Modifier = Modifier) {
    WeatherNavigationRail(modifier = modifier) {

        destinations.forEach { destination ->

            val selected = currentDestination?.isTopLevelDestination(destination)

            NavigationRailItem(
                icon = {
                    Icon(imageVector = ImageVector.vectorResource(destination.icon), contentDescription = null)
                },
                selected = selected ?: false,
                onClick = { onNavigateTopLevelDestination(destination) },
                modifier = modifier
            )

        }
    }
}

private fun NavDestination?.isTopLevelDestination(destination: TopLevelDestination): Boolean =
    this?.hierarchy?.any { it.route?.contains(destination.name, ignoreCase = true) ?: false } ?: false