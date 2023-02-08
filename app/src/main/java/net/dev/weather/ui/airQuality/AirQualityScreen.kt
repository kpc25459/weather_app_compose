package net.dev.weather.ui.airQuality

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.dev.weather.R
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.datetime.LocalDateTime
import net.dev.weather.api.WeatherServiceApi
import net.dev.weather.components.ErrorScreen
import net.dev.weather.components.LoadingScreen
import net.dev.weather.data.AirPollutionForecast
import net.dev.weather.data.NetworkRepository
import net.dev.weather.fromAqiIndex
import net.dev.weather.imageFromAqi

@Composable
fun AirQualityScreen(modifier: Modifier = Modifier, viewModel: AirQualityViewModel = AirQualityViewModel(NetworkRepository(WeatherServiceApi.create()))) {

    val lifecycle = LocalLifecycleOwner.current.lifecycle

    val uiState by produceState<AirQualityUiState>(
        initialValue = AirQualityUiState.Loading,
        key1 = lifecycle,
        key2 = viewModel
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            viewModel.uiState.collect { value = it }
        }
    }

    if (uiState is AirQualityUiState.Loading) {
        LoadingScreen()
    }

    if (uiState is AirQualityUiState.Success) {
        AirQualityScreen(
            data = (uiState as AirQualityUiState.Success).data,
            modifier = modifier
        )
    }

    if (uiState is AirQualityUiState.Error) {
        ErrorScreen()
    }
}

@Composable
internal fun AirQualityScreen(data: AirPollution, modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .padding(5.dp)
        /*.verticalScroll(rememberScrollState())*/
    ) {
        Box(data)
        Spacer(modifier = Modifier.height(20.dp))
        HourPollutionForecast(data.forecast)
    }
}

@Composable
fun Box(data: AirPollution) {
    val currentWeather = data.forecast.first()

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
                Text(text = data.location, color = Color.White)
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
    AirQualityScreen(
        AirPollution(
            "Paris", listOf(
                AirPollutionForecast(
                    dt = LocalDateTime(2021, 5, 1, 10, 0),
                    co = 607.49,
                    no = 14.53,
                    no2 = 23.31,
                    o3 = 0.08,
                    so2 = 23.84,
                    pm2_5 = 40.52,
                    pm10 = 49.45,
                    nh3 = 1.5,
                    aqi = 4
                ),
                AirPollutionForecast(
                    dt = LocalDateTime(2021, 5, 1, 11, 0),
                    co = 594.14,
                    no = 12.41,
                    no2 = 23.99,
                    o3 = 0.13,
                    so2 = 21.7,
                    pm2_5 = 39.76,
                    pm10 = 47.98,
                    nh3 = 1.38,
                    aqi = 2
                )
            )
        )
    )
}

