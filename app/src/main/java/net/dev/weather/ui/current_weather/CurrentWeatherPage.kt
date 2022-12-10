package net.dev.weather.ui.current_weather

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.dev.weather.api.WeatherServiceApi
import net.dev.weather.data.WeatherRepository
import net.dev.weather.viewmodels.CurrentWeatherViewModel

@Composable
fun CurrentWeatherPage() {

    val repository = WeatherRepository(WeatherServiceApi.create())

    val viewModel = CurrentWeatherViewModel(repository)

    Column {
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
    CurrentWeatherPage()
}
