package net.dev.weather.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.dev.weather.api.WeatherServiceApi
import net.dev.weather.data.WeatherRepository
import net.dev.weather.components.Box
import net.dev.weather.components.CurrentWeatherDetails
import net.dev.weather.components.HourForecast
import net.dev.weather.data.NetworkRepository
import net.dev.weather.ui.currentWeather.CurrentWeatherViewModel

@Composable
fun CurrentWeatherScreen() {

    val repository = NetworkRepository(WeatherServiceApi.create())

    val viewModel = CurrentWeatherViewModel(repository)

    Column(modifier = Modifier.padding(5.dp).verticalScroll(rememberScrollState())) {
        Box(viewModel)
        Spacer(modifier = Modifier.height(20.dp))
        HourForecast(viewModel)
        Spacer(modifier = Modifier.height(20.dp))
        CurrentWeatherDetails(viewModel)
    }
}

@Preview(showBackground = true)
@Composable
fun CurrentWeatherPagePreview() {
    CurrentWeatherScreen()
}
