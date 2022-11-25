package net.dev.weather.ui.current_weather

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.datetime.Instant
import kotlinx.datetime.toLocalDateTime
import net.dev.weather.viewmodels.CurrentWeatherViewModel
import kotlin.math.roundToInt

@Composable
fun Box(viewModel: CurrentWeatherViewModel) {
    val timeZone by viewModel.timeZone.observeAsState()
    val location by viewModel.location.observeAsState()
    val currentWeather by viewModel.currentWeather.observeAsState()
    val airQuality by viewModel.airQuality.observeAsState()

    timeZone ?: return
    location ?: return
    currentWeather ?: return
    airQuality ?: return

    val at = Instant.fromEpochSeconds(currentWeather!!.dt.toLong()).toLocalDateTime(timeZone!!)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        elevation = 8.dp,
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "Prognoza na: ${at.date}")
                Text(text = "Godz. ${at.time.toString().substringBeforeLast(":")}")
            }
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(imageVector = Icons.Filled.Place, contentDescription = "Place")
                    location?.let { Text(text = it) }
                }
                Text(text = "${currentWeather!!.temp.roundToInt()} °C")
            }
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Jakość powietrza")
                Text(text = airQuality!!)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BoxPreview() {
    Box(CurrentWeatherViewModel())
}
