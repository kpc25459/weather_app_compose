package net.dev.weather.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.dev.weather.ui.currentWeather.CurrentWeatherViewModel
import kotlin.math.roundToInt
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import net.dev.weather.data.WeatherHourly

@Composable
fun HourForecast(currentWeather: CurrentWeatherViewModel) {

    val hourlyForecast by currentWeather.hourlyForecast.observeAsState()

    hourlyForecast?.let { forecast ->
        LazyRow(/*modifier = Modifier.horizontalScroll(rememberScrollState())*/) {
            items(forecast) { item ->
               HourForecastItem(item)
            }
        }
    }

}

@Composable
fun HourForecastItem(item: WeatherHourly) {
    val dayOfWeek = "${item.dt.dayOfWeek.toString().lowercase().substring(0, 3).replaceFirstChar { it.uppercase() }}."

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(5.dp)
            .border(1.dp, shape = RoundedCornerShape(8.dp), color = Color(0xFFE0EAFF))
            .width(70.dp)
    ) {
        Text(text = dayOfWeek)
        WeatherIcon(item.weather[0].icon)
        Text(text = item.dt.time.toString())
        Text(text = "${item.temp.roundToInt()}°", modifier = Modifier.padding(bottom = 5.dp))
    }

}

@Preview(showBackground = true)
@Composable
fun HourForecastPreview() {
    //HourForecast()
}
