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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
        contentPadding = PaddingValues(bottom = 50.dp)
    ) {
        itemsIndexed(data.daily.drop(1)) { idx, weatherDaily ->
            DayForecastItem(weatherDaily, initiallyExpanded = idx == 0, modifier = modifier)
            Spacer(modifier = Modifier.height(5.dp))
            HorizontalDivider()
        }
    }
}

/*
@Preview(showBackground = true)
@Composable
fun WeatherForecastScreenPreview() {
    WeatherForecastScreen(data = sampleMain)
}
*/
