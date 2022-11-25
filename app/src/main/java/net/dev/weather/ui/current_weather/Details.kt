package net.dev.weather.ui.current_weather

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.datetime.Instant
import kotlinx.datetime.toLocalDateTime
import net.dev.weather.viewmodels.CurrentWeatherViewModel
import kotlin.math.roundToInt

@Composable
fun CurrentWeatherDetails(viewModel: CurrentWeatherViewModel) {

    val timeZone by viewModel.timeZone.observeAsState()
    val currentWeather by viewModel.currentWeather.observeAsState()
    val hourlyForecast by viewModel.hourlyForecast.observeAsState()


    currentWeather?.let { weather ->
        Row(modifier = Modifier.padding(10.dp)) {
            Row {
                Column {
                    val sunrise = timeZone?.let { Instant.fromEpochSeconds(weather.sunrise.toLong()).toLocalDateTime(it) }?.time
                    DetailsItem(
                        image = Icons.Filled.ArrowForward,
                        name = "Wschód słońca",
                        value = sunrise?.toString()?.substringBeforeLast(":") ?: "N/A"
                    )
                    DetailsItem(
                        image = Icons.Filled.ArrowForward,
                        name = "Ciśnienie",
                        value = "${weather.pressure} hPa"
                    )
                    DetailsItem(
                        image = Icons.Filled.ArrowForward,
                        name = "Indeks UV",
                        value = weather.uvi.roundToInt().toString(),
                    )
                    DetailsItem(
                        image = Icons.Filled.ArrowForward,
                        name = "Deszcz",
                        value = "??? mm/24h",
                    )
                }

                Column {
                    val sunset = timeZone?.let { Instant.fromEpochSeconds(weather.sunset.toLong()).toLocalDateTime(it) }?.time

                    DetailsItem(
                        image = Icons.Filled.ArrowForward,
                        name = "Zachód słońca",
                        value = sunset?.toString()?.substringBeforeLast(":") ?: "N/A"
                    )
                    DetailsItem(
                        image = Icons.Filled.ArrowForward,
                        name = "Wilgotność",
                        value = "${weather.humidity} %"
                    )
                    DetailsItem(
                        image = Icons.Filled.ArrowForward,
                        name = "Wiatr",
                        value = "${((weather.wind_speed * 3.6 * 100) / 100).roundToInt()} km/h ${toHuman(weather.wind_deg)}",
                    )

                    hourlyForecast?.let {
                        DetailsItem(
                            image = Icons.Filled.ArrowForward,
                            name = "Odczuwana",
                            value = "${weather.feels_like.roundToInt()}°",
                        )
                    }
                }
            }
        }
    }
}

private fun toHuman(deg: Int): String {
    if (deg > 337.5) {
        return "N";
    } else if (deg > 292.5) {
        return "NW";
    } else if (deg > 247.5) {
        return "W";
    } else if (deg > 202.5) {
        return "SW";
    } else if (deg > 157.5) {
        return "S";
    } else if (deg > 122.5) {
        return "SE";
    } else if (deg > 67.5) {
        return "E";
    } else if (deg > 22.5) {
        return "NE";
    } else {
        return "";
    }
}

@Composable
fun DetailsItem(image: ImageVector, name: String, value: String) {
    Row(
        modifier = Modifier
            .padding(10.dp)
    ) {
        Icon(imageVector = image, contentDescription = null)
        Column(modifier = Modifier.padding(start = 10.dp)) {
            Text(text = name)
            Text(text = value)
        }
    }

}

@Preview(showBackground = true)
@Composable
fun CurrentWeatherDetailsPreview() {
    /*Column {
        CurrentWeatherDetails(viewModel)
    }*/
}
