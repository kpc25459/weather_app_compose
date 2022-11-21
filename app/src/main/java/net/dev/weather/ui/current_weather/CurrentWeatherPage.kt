package net.dev.weather.ui.current_weather

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.dev.weather.viewmodels.CurrentWeatherViewModel

@Composable
fun CurrentWeatherPage() {

    val viewModel = CurrentWeatherViewModel()

    Column {

        Box(viewModel)
        Spacer(modifier = Modifier.height(20.dp))
        HourForecast(viewModel)
        Spacer(modifier = Modifier.height(20.dp))
        CurrentWeatherDetails()
    }
}

@Preview(showBackground = true)
@Composable
fun CurrentWeatherPagePreview() {
    CurrentWeatherPage()
}
