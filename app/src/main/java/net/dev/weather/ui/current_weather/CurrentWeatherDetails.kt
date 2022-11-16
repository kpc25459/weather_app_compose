package net.dev.weather.ui.current_weather

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CurrentWeatherDetails() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        elevation = 8.dp,
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
           Text(text = "Szczegóły")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CurrentWeatherDetailsPreview() {
    CurrentWeatherDetails()
}
