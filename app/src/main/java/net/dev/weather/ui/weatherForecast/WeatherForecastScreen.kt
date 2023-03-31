package net.dev.weather.ui.weatherForecast

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import net.dev.weather.MainViewModel
import net.dev.weather.NavRoutes
import net.dev.weather.R
import net.dev.weather.api.WeatherServiceApi
import net.dev.weather.bottomNavigationBar
import net.dev.weather.data.Main
import net.dev.weather.data.NetworkRepository
import net.dev.weather.getTitleByRoute
import net.dev.weather.theme.tabBarBackgroundColor
import net.dev.weather.theme.tabBarTextColor

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun WeatherForecastScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = MainViewModel(NetworkRepository(WeatherServiceApi.create())),
    scaffoldState: ScaffoldState = rememberScaffoldState()
) {

    Scaffold(
        topBar = topBar(),
        bottomBar = bottomNavigationBar(navController = navController),
        scaffoldState = scaffoldState,
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->

        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        uiState.main?.let { main ->
            Content(main, modifier = Modifier.padding(paddingValues))
        }
    }
}

@Composable
private fun topBar(): @Composable () -> Unit {
    return {
        TopAppBar(
            title = { Text(text = stringResource(id = R.string.weather_forecast_screen_title)) },
            backgroundColor = tabBarBackgroundColor,
            contentColor = tabBarTextColor,
            elevation = 0.dp,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
@OptIn(ExperimentalLifecycleComposeApi::class)
private fun Content(data: Main, modifier: Modifier) {
    LazyColumn {
        itemsIndexed(data.daily.drop(1)) { idx, weatherDaily ->
            DayForecastItem(weatherDaily, initiallyExpanded = idx == 0, modifier = modifier)
            Spacer(modifier = Modifier.height(5.dp))
            Divider()
        }
    }
}

/*
@Preview(showBackground = true)
@Composable
fun WeatherForecastScreenPreview() {
    WeatherForecastScreen(data = sampleMain)
}
*/
