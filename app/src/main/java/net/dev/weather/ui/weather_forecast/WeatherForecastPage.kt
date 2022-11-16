package net.dev.weather.ui.weather_forecast

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun WeatherForecastPage() {
    Text(text = "WeatherForecast")
}

@Preview(showBackground = true)
@Composable
fun WeatherForecastPagePreview() {
    WeatherForecastPage()
}
