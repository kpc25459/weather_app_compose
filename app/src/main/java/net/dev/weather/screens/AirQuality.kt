package net.dev.weather.screens

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun AirQualityScreen() {
    Text(text = "AirQualityPage")
}

@Preview(showBackground = true)
@Composable
fun AirQualityPagePreview() {
    AirQualityScreen()
}
