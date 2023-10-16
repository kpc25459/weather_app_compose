package net.dev.weather.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import net.dev.weather.components.WeatherBottomBar
import net.dev.weather.components.WeatherTopAppBar
import net.dev.weather.navigation.WeatherNavHost

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherApp(
    appState: WeatherAppState = rememberWeatherAppState(),
) {
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {

        Scaffold(
            bottomBar = {
                WeatherBottomBar(
                    destinations = appState.topLevelDestinations,
                    onNavigateTopLevelDestination = appState::navigateToTopLevelDestination,
                    currentDestination = appState.currentDestination
                )
            },
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
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