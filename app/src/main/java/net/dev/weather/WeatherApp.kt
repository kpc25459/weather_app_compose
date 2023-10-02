package net.dev.weather

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import net.dev.weather.navigation.WeatherNavHost

@Composable
fun WeatherApp() {
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
        WeatherNavHost()
    }
}