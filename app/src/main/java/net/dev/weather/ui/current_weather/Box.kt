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
import net.dev.weather.viewmodels.CurrentWeatherViewModel

@Composable
fun Box(viewModel: CurrentWeatherViewModel) {
    val location by viewModel.location.observeAsState()
    val currentWeather by viewModel.currentWeather.observeAsState()
    val airQuality by viewModel.airQuality.observeAsState()

    //TODO: tutaj dodać kręciołki
    location ?: return
    currentWeather ?: return
    airQuality ?: return

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        elevation = 8.dp,
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "Prognoza na: ${currentWeather!!.dt}")
                Text(text = "Godz. ${currentWeather!!.dt.time.toString().substringBeforeLast(":")}")
            }
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(imageVector = Icons.Filled.Place, contentDescription = "Place")
                    location?.let { Text(text = it) }
                }
                Text(text = "${currentWeather!!.temp} °C")
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
    //Box(CurrentWeatherViewModel())
}
