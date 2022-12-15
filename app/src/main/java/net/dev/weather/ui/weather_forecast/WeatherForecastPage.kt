@file:OptIn(ExperimentalMaterialApi::class)

package net.dev.weather.ui.weather_forecast

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import net.dev.weather.api.WeatherServiceApi
import net.dev.weather.data.WeatherHourly
import net.dev.weather.data.WeatherRepository
import net.dev.weather.viewmodels.CurrentWeatherViewModel
import java.time.format.TextStyle
import java.util.*
import kotlin.math.roundToInt

@Composable
fun WeatherForecastPage() {

    val repository = WeatherRepository(WeatherServiceApi.create())

    val viewModel = CurrentWeatherViewModel(repository)

    val hourlyForecast by viewModel.hourlyForecast.observeAsState()

    hourlyForecast?.let { forecast ->
        LazyColumn {
            items(forecast) { item ->
                DayForecastItem(item)
                Divider()
            }
        }
    }
}

@Composable
fun DayForecastItem(item: WeatherHourly) {

    //TODO: tutaj przerobić nazwy miesiąca na ładne polskie
    ListItem(
        text = {
            Text(
                text = "${item.dt.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())}, ${item.dt.dayOfMonth} ${
                    item.dt.month.getDisplayName(
                        TextStyle.FULL,
                        Locale.getDefault()
                    )
                }"
            )
        },
        secondaryText = {
            Text(text = item.weather[0].description)
        },
        icon = { Icon(Icons.Filled.Favorite, contentDescription = null) },
        trailing = {
            //  Text(text = "${item.temp.roundToInt()}°C")

            Icon(
                Icons.Filled.KeyboardArrowDown,
                contentDescription = "Localized Description"
            )

        },

        )
}
