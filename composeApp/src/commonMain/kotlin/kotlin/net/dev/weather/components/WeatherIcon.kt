package kotlin.net.dev.weather.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

@Composable
fun WeatherIcon(icon: String, onClick: () -> Unit = {}) {
    val interactionSource = remember { MutableInteractionSource() }

    val iconUrl = "https://openweathermap.org/img/wn/${icon}.png"

    Image(
        painter = rememberAsyncImagePainter(iconUrl),
        contentDescription = "weather icon",
        contentScale = ContentScale.Fit,
        modifier = Modifier
            .size(50.dp)
            .clickable(indication = null, interactionSource = interactionSource) { onClick() }
    )
}