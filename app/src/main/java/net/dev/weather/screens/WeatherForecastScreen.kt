@file:OptIn(ExperimentalLifecycleComposeApi::class)

package net.dev.weather.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import net.dev.weather.api.WeatherServiceApi
import net.dev.weather.components.DayForecastItem
import net.dev.weather.data.ExpandableCardModel
import net.dev.weather.data.WeatherRepository
import net.dev.weather.viewmodels.WeatherForecastViewModel
import java.util.*

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun WeatherForecastScreen() {

    val repository = WeatherRepository(WeatherServiceApi.create())

    val viewModel = WeatherForecastViewModel(repository)

    val cards by viewModel.cards.collectAsStateWithLifecycle()

    LazyColumn {
        items(cards) { weatherDaily ->
            DayForecastItem(weatherDaily)
            Spacer(modifier = Modifier.height(5.dp))
            Divider()
        }
    }
}


