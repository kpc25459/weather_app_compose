package net.dev.weather.ui.weatherForecast

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.dev.weather.Main
import net.dev.weather.sampleMain


@Composable
fun WeatherForecastScreen(data: Main, modifier: Modifier = Modifier) {
    LazyColumn {
        itemsIndexed(data.daily.drop(1)) { idx, weatherDaily ->
            DayForecastItem(weatherDaily, initiallyExpanded =  idx == 0, modifier = modifier)
            Spacer(modifier = Modifier.height(5.dp))
            Divider()
        }
    }
}


@Preview(showBackground = true)
@Composable
fun WeatherForecastScreenPreview() {
    WeatherForecastScreen(data = sampleMain)
}
