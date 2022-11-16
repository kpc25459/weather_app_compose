package net.dev.weather.ui.current_weather

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CurrentWeatherPage() {
    Column {
        Box()
        Spacer(modifier = Modifier.height(20.dp))
        HourForecast()
        Spacer(modifier = Modifier.height(20.dp))
        CurrentWeatherDetails()
    }
}

@Preview(showBackground = true)
@Composable
fun CurrentWeatherPagePreview() {
    CurrentWeatherPage()
}
