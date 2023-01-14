package net.dev.weather.ui.weatherForecast

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle.State.STARTED
import androidx.lifecycle.repeatOnLifecycle
import net.dev.weather.api.WeatherServiceApi
import net.dev.weather.components.DayForecastItem
import net.dev.weather.components.ErrorScreen
import net.dev.weather.components.LoadingScreen
import net.dev.weather.data.NetworkRepository
import net.dev.weather.data.WeatherDaily

@Composable
fun WeatherForecastScreen(modifier: Modifier = Modifier, viewModel: WeatherForecastViewModel = WeatherForecastViewModel(NetworkRepository(WeatherServiceApi.create()))) {

    val lifecycle = LocalLifecycleOwner.current.lifecycle

    val items by produceState<WeatherForecastUiState>(
        initialValue = WeatherForecastUiState.Loading,
        key1 = lifecycle,
        key2 = viewModel
    ) {
        lifecycle.repeatOnLifecycle(state = STARTED) {
            viewModel.uiState.collect { value = it }
        }
    }

    if (items is WeatherForecastUiState.Loading) {
        LoadingScreen()
    }

    if (items is WeatherForecastUiState.Success) {
        WeatherForecastScreen(
            items = (items as WeatherForecastUiState.Success).data,
            modifier = modifier
        )
    }

    if (items is WeatherForecastUiState.Error) {
        ErrorScreen()
    }
}

@Composable
internal fun WeatherForecastScreen(items: List<WeatherDaily>, modifier: Modifier = Modifier) {
    LazyColumn {
        items(items) { weatherDaily ->
            DayForecastItem(weatherDaily, modifier = modifier)
            Spacer(modifier = Modifier.height(5.dp))
            Divider()
        }
    }
}