package net.dev.weather.ui.current_weather

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.dev.weather.api.WeatherHourly
import net.dev.weather.viewmodels.CurrentWeatherViewModel
import net.dev.weather.viewmodels.HourlyForecast

@Composable
fun HourForecast(currentWeather: CurrentWeatherViewModel) {

    val scrollState = rememberScrollState()

    val hourlyForecast by currentWeather.hourlyForecast.observeAsState()

    hourlyForecast?.let { forecast ->
        Row(Modifier.horizontalScroll(scrollState)) {
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(10.dp)
            ) {
                items(forecast) { item ->
                    HourForecastItem(item)
                }
            }
        }
    }

}

@Composable
fun HourForecastItem(item: WeatherHourly) {
    Text(text = "Czw.")
    Icon(imageVector = Icons.Filled.Close, contentDescription = null)
    Text(text = "20:00")
    Text(text = "${item.temp}°")
}

@Preview(showBackground = true)
@Composable
fun HourForecastPreview() {
    //HourForecast()
}
