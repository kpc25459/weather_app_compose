package net.dev.weather.ui.current_weather

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
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
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import net.dev.weather.api.WeatherHourly
import net.dev.weather.viewmodels.CurrentWeatherViewModel
import java.util.*
import kotlin.math.roundToInt

@Composable
fun HourForecast(currentWeather: CurrentWeatherViewModel) {

    val hourlyForecast by currentWeather.hourlyForecast.observeAsState()
    val timeZone by currentWeather.timeZone.observeAsState()

    hourlyForecast?.let { forecast ->
        LazyRow(/*modifier = Modifier.horizontalScroll(rememberScrollState())*/) {
            items(forecast) { item ->
                timeZone?.let { HourForecastItem(item, it) }
            }
        }
    }

}

@Composable
fun HourForecastItem(item: WeatherHourly, timeZone: TimeZone) {
    val dateTime = Instant.fromEpochSeconds(item.dt.toLong()).toLocalDateTime(timeZone)

    val dayOfWeek = "${dateTime.date.dayOfWeek.toString().lowercase(Locale.getDefault()).substring(0, 3).replaceFirstChar { it.uppercase() }}."

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(10.dp)) {
        Text(text = dayOfWeek)
        Icon(imageVector = Icons.Filled.Close, contentDescription = null)
        Text(text = dateTime.time.toString())
        Text(text = "${item.temp.roundToInt()}°")
    }

}

@Preview(showBackground = true)
@Composable
fun HourForecastPreview() {
    //HourForecast()
}
