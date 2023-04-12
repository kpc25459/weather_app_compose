package net.dev.weather.ui.weatherForecast

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import net.dev.weather.R
import net.dev.weather.api.WeatherServiceApi
import net.dev.weather.bottomNavigationBar
import net.dev.weather.data.NetworkRepository
import net.dev.weather.data.WeatherForecast
import net.dev.weather.theme.tabBarBackgroundColor
import net.dev.weather.theme.tabBarTextColor

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun WeatherForecastScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: WeatherForecastViewModel = hiltViewModel(),
    scaffoldState: ScaffoldState = rememberScaffoldState()
) {

    Scaffold(
        topBar = topBar(),
        bottomBar = bottomNavigationBar(navController = navController),
        scaffoldState = scaffoldState,
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->

        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        uiState.weatherForecast?.let {
            Content(it, modifier = Modifier.padding(paddingValues))
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
private fun Content(data: WeatherForecast, modifier: Modifier) {
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
