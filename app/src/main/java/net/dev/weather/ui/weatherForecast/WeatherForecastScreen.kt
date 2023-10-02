package net.dev.weather.ui.weatherForecast

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import net.dev.weather.R
import net.dev.weather.ui.bottomNavigationBar
import net.dev.weather.components.WeatherTopAppBar
import net.dev.weather.data.model.WeatherForecast

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherForecastScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: WeatherForecastViewModel = hiltViewModel()
) {

    Scaffold(
        topBar = { WeatherTopAppBar(titleRes = R.string.weather_forecast_screen_title) },
        bottomBar = bottomNavigationBar(navController = navController),
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->

        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        uiState.weatherForecast?.let {
            Content(it, modifier = Modifier.padding(paddingValues))
        }
    }
}

@Composable
private fun Content(data: WeatherForecast, modifier: Modifier) {
    LazyColumn(
        contentPadding = PaddingValues(bottom = 50.dp)
    ) {
        itemsIndexed(data.daily.drop(1)) { idx, weatherDaily ->
            DayForecastItem(weatherDaily, initiallyExpanded = idx == 0, modifier = modifier)
            Spacer(modifier = Modifier.height(5.dp))
            HorizontalDivider()
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
