package net.dev.weather.ui.hour_forecast

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun HourForecast() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        elevation = 8.dp,
    ) {
       Text(text = "Prognoza godzinowa")
    }
}

@Preview(showBackground = true)
@Composable
fun HourForecastPreview() {
    HourForecast()
}
