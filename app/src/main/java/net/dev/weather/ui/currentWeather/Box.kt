package net.dev.weather.ui.currentWeather

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
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
/*

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
        Image(
            painterResource(id = currentWeather!!.backgroundImage),
            contentDescription = "Weather condition",
            contentScale = ContentScale.Fit,
            colorFilter = ColorFilter.tint(Color.Black.copy(alpha = 0.5f), blendMode = BlendMode.SrcOver),
            modifier = Modifier
                .fillMaxWidth()
        )

        Column(modifier = Modifier.padding(10.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "Prognoza na: ${currentWeather!!.dt.date}", color = Color.White)
                Text(text = "Godz. ${currentWeather!!.dt.time.toString().substringBeforeLast(":")}", color = Color.White)
            }
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(imageVector = Icons.Filled.Place, contentDescription = "Place", colorFilter = ColorFilter.tint(Color.White))
                    location?.let { Text(text = it, color = Color.White) }
                }
                Text(text = "${currentWeather!!.temp} °C", color = Color.White)
            }
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Jakość powietrza", color = Color.White)
                Text(text = airQuality!!, color = Color.White)
            }
        }
    }
}*/
