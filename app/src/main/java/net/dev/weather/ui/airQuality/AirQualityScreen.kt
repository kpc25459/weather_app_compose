package net.dev.weather.ui.airQuality

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.datetime.LocalDateTime
import net.dev.weather.*
import net.dev.weather.R
import net.dev.weather.data.AirPollutionForecast
import net.dev.weather.data.WeatherCurrent

@Composable
fun AirQualityScreen(data: Main, modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .padding(5.dp)
        /*.verticalScroll(rememberScrollState())*/
    ) {
        Box(data.location, data.airPollutionForecast)
        Spacer(modifier = Modifier.height(20.dp))
        HourPollutionForecast(data.airPollutionForecast)
    }
}

@Composable
fun Box(location: String, data: List<AirPollutionForecast>) {
    val currentWeather = data.first()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(horizontal = 8.dp),
        elevation = 8.dp
    ) {
        Image(
            painterResource(id = imageFromAqi(currentWeather.aqi)),
            contentDescription = stringResource(R.string.weather_condition),
            contentScale = ContentScale.Crop,
            colorFilter = ColorFilter.tint(Color.Black.copy(alpha = 0.5f), blendMode = BlendMode.SrcOver),
            modifier = Modifier
                .fillMaxWidth()
        )

        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(10.dp).fillMaxWidth()) {
            Row {
                Image(painter = painterResource(R.drawable.outline_location_on_24), contentDescription = stringResource(R.string.place), colorFilter = ColorFilter.tint(Color.White))
                Text(text = location, color = Color.White)
            }

            Row {
                Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = fromAqiIndex(currentWeather.aqi), color = Color.White)
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(text = stringResource(R.string.pm25_extended, currentWeather.pm2_5), color = Color.White)
                Text(text = stringResource(R.string.pm10_extended, currentWeather.pm10), color = Color.White)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AirQualityScreenPreview() {
    AirQualityScreen(data = sampleMain)
}

