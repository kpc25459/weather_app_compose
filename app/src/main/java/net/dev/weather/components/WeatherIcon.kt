package net.dev.weather.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter

@Composable
fun WeatherIcon(icon: String) {

    val iconUrl = "https://openweathermap.org/img/wn/${icon}.png"

    Image(
        painter = rememberImagePainter(iconUrl),
        contentDescription = "weather icon",
        contentScale = ContentScale.Fit,
        modifier = Modifier.size(50.dp)
    )
}