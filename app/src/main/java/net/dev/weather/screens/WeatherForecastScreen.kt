@file:OptIn(ExperimentalLifecycleComposeApi::class)

package net.dev.weather.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.datetime.LocalDateTime
import net.dev.weather.api.WeatherServiceApi
import net.dev.weather.components.ExpandableCard
import net.dev.weather.data.ExpandableCardModel
import net.dev.weather.data.WeatherRepository
import net.dev.weather.viewmodels.WeatherForecastViewModel
import java.time.format.TextStyle
import java.util.*

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun WeatherForecastScreen() {

    val repository = WeatherRepository(WeatherServiceApi.create())

    val viewModel = WeatherForecastViewModel(repository)

    val cards by viewModel.cards.collectAsStateWithLifecycle()
    val expandedCardIds by viewModel.expandedCardIdsList.collectAsStateWithLifecycle()

    LazyColumn {
        items(cards, ExpandableCardModel::id) { card ->
            ExpandableCard(
                card = card,
                onCardArrowClick = { viewModel.onCardArrowClicked(card.id) },
                expanded = expandedCardIds.contains(card.id)
            )
            //DayForecastItem(card)
            Divider()
        }
    }
}



@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DayForecastItem(card: ExpandableCardModel) {

    val item = card.weatherHourly

    //TODO: tutaj przerobić nazwy miesiąca na ładne polskie
    ListItem(
        text = {
            Text(
                text = localDate(item.dt)
            )
        },
        secondaryText = {
            Text(text = item.weather[0].description)
        },
        icon = { Icon(Icons.Filled.Favorite, contentDescription = null) },
        trailing = {
            //  Text(text = "${item.temp.roundToInt()}°C")

            Icon(
                Icons.Filled.KeyboardArrowDown, contentDescription = "Localized Description"
            )

        }
    )
}

private fun localDate(dt: LocalDateTime) = "${dt.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())}, ${dt.dayOfMonth} ${
    dt.month.getDisplayName(
        TextStyle.FULL, Locale.getDefault()
    )
}"
