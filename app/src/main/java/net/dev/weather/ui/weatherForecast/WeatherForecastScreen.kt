package net.dev.weather.ui.weatherForecast

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import net.dev.weather.data.model.WeatherDaily
import net.dev.weather.data.model.WeatherForecast

@Composable
fun WeatherForecastScreen(
    modifier: Modifier = Modifier,
    viewModel: WeatherForecastViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    uiState.weatherForecast?.let {
        Content(it)
    }
}

@Composable
private fun Content(data: WeatherForecast, modifier: Modifier = Modifier) {
    LazyColumn(
        contentPadding = PaddingValues(bottom = 50.dp),
    ) {
        itemsIndexed(data.daily.drop(1)) { idx, weatherDaily ->
            DayForecastItem(weatherDaily, initiallyExpanded = idx == 0, modifier = modifier)
            Spacer(modifier = Modifier.height(5.dp))
            HorizontalDivider()
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
