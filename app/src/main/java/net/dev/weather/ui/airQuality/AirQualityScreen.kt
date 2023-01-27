package net.dev.weather.ui.airQuality

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Place
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.datetime.LocalDateTime
import net.dev.weather.R
import net.dev.weather.api.WeatherServiceApi
import net.dev.weather.components.ErrorScreen
import net.dev.weather.components.LoadingScreen
import net.dev.weather.components.WeatherIcon
import net.dev.weather.data.AirPollutionForecast
import net.dev.weather.data.NetworkRepository
import net.dev.weather.data.WeatherDaily
import net.dev.weather.localDate

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
            .verticalScroll(rememberScrollState())
    ) {
        Box(data)
        Spacer(modifier = Modifier.height(20.dp))
        HourPollutionForecast(data.forecast)
        Spacer(modifier = Modifier.height(20.dp))
        Chart(data)
    }
}

@Composable
fun Box(data: AirPollution) {
    val currentWeather = data.forecast.first()
    val airQuality = currentWeather.airQuality

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        elevation = 8.dp,
    ) {
        Image(
            painterResource(id = R.drawable.new_thunder/* currentWeather.backgroundImage*/),
            contentDescription = "Weather condition",
            contentScale = ContentScale.Fit,
            colorFilter = ColorFilter.tint(Color.Black.copy(alpha = 0.5f), blendMode = BlendMode.SrcOver),
            modifier = Modifier
                .fillMaxWidth()
        )

        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(10.dp).fillMaxWidth()) {
            Row {
                Image(imageVector = Icons.Filled.Place, contentDescription = "Place", colorFilter = ColorFilter.tint(Color.White))
                Text(text = data.location, color = Color.White)
            }

            Row {
                Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = airQuality, color = Color.White)
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(text = "PM 2.5: ${currentWeather.pm2_5}", color = Color.White)
                Text(text = "PM 10: ${currentWeather.pm10}", color = Color.White)
            }
        }
    }
}


@Composable
fun Chart(data: AirPollution) {

}


@Preview(showBackground = true)
@Composable
fun AirQualityScreenPreview() {
    AirQualityScreen(
        AirPollution(
            "Paris", listOf(
                AirPollutionForecast(
                    dt = LocalDateTime(2021, 5, 1, 10, 0),
                    airQuality = "Bardzo dobra",
                    co = 607.49,
                    no = 14.53,
                    no2 = 23.31,
                    o3 = 0.08,
                    so2 = 23.84,
                    pm2_5 = 40.52,
                    pm10 = 49.45,
                    nh3 = 1.5
                ),
                AirPollutionForecast(
                    dt = LocalDateTime(2021, 5, 1, 11, 0),
                    airQuality = "Średnia",
                    co = 594.14,
                    no = 12.41,
                    no2 = 23.99,
                    o3 = 0.13,
                    so2 = 21.7,
                    pm2_5 = 39.76,
                    pm10 = 47.98,
                    nh3 = 1.38
                )
            )
        )
    )
}

