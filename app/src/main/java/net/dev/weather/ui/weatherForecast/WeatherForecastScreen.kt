package net.dev.weather.ui.weatherForecast

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import net.dev.weather.R
import net.dev.weather.components.LoadingScreen
import net.dev.weather.components.WeatherIcon
import net.dev.weather.data.model.WeatherDaily
import net.dev.weather.data.model.WeatherForecast
import net.dev.weather.utils.localDate
import kotlin.math.roundToInt

@Composable
fun WeatherForecastScreen(
    modifier: Modifier = Modifier,
    viewModel: WeatherForecastViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    if (uiState.isLoading) {
        LoadingScreen()
    } else {
        uiState.weatherForecast?.let {
            Content(it)
        }
    }
}

@Composable
private fun Content(data: WeatherForecast, modifier: Modifier = Modifier) {
    LazyColumn(
        contentPadding = PaddingValues(bottom = 50.dp),
    ) {
        item {
            ExpandableListItems(
                items = data.daily.drop(1),
                headlineContent = { weatherDaily ->
                    Text(text = localDate(weatherDaily.dt))
                },
                content = { weatherDaily ->
                    Text(text = weatherDaily.description)
                },
                leadingContent = { weatherDaily -> WeatherIcon(weatherDaily.icon) },
                modifier = modifier,
                expandedContent = { weatherDaily ->
                    ListItemRow(label = stringResource(R.string.temperature), value = "${weatherDaily.tempDay.roundToInt()}°C / ${weatherDaily.tempNight.roundToInt()}°C")
                    ListItemRow(label = stringResource(R.string.sunrise), value = weatherDaily.sunrise.toString().substringBeforeLast(":"))
                    ListItemRow(label = stringResource(R.string.sunset), value = weatherDaily.sunset.toString().substringBeforeLast(":"))
                    ListItemRow(label = stringResource(R.string.pressure), value = "${weatherDaily.pressure} hPa")
                    ListItemRow(label = stringResource(R.string.humidity), value = "${weatherDaily.humidity} %")
                    ListItemRow(label = stringResource(R.string.wind), value = "${weatherDaily.wind.roundToInt()} km/h ${weatherDaily.windDirection}")
                    ListItemRow(label = stringResource(R.string.rain), value = "${weatherDaily.rain.roundToInt()} mm/24h")
                    ListItemRow(label = stringResource(R.string.uv_Index), value = weatherDaily.uvi.roundToInt().toString())
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ContentPreview() {
    Content(
        WeatherForecast(
            listOf(sampleDaily2, sampleDaily2, sampleDaily2, sampleDaily2, sampleDaily2)
        )
    )
}

var sampleDaily2: WeatherDaily = WeatherDaily(
    dt = LocalDateTime(2023, 5, 29, 12, 0),
    description = "zachmurzenie małe",
    sunrise = LocalTime(4, 40),
    sunset = LocalTime(21, 0),
    tempDay = 19.0,
    tempNight = 15.0,
    pressure = 1020,
    humidity = 53,
    wind = 17.0,
    windDirection = "NE",
    rain = 0.0,
    uvi = 6.0,
    icon = "03d"
)
