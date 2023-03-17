package net.dev.weather.ui.airQuality

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.datetime.LocalDateTime
import net.dev.weather.R
import net.dev.weather.ui.model.UiAirPollutionForecast
import net.dev.weather.fromAqiIndex

@Composable
fun HourPollutionForecast(forecast: List<UiAirPollutionForecast>) {
    LazyColumn(modifier = Modifier.height(320.dp)) {
        items(forecast) { item ->
            HourForecastItem(item)
            Spacer(modifier = Modifier.height(5.dp))
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HourForecastItem(item: UiAirPollutionForecast, modifier: Modifier = Modifier) {

    var expanded by rememberSaveable { mutableStateOf(false) }

    val onClick = { expanded = !expanded }

    val interactionSource = remember { MutableInteractionSource() }

    ListItem(
        text = { Text(text = item.dt, style = MaterialTheme.typography.body1, modifier = Modifier.clickable(indication = null, interactionSource = interactionSource) { onClick() }) },
        secondaryText = {
            Column {
                Text(
                    text = stringResource(R.string.air_quality_with_value, fromAqiIndex(item.aqi)),
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.clickable(indication = null, interactionSource = interactionSource) { onClick() })

                if (expanded) {
                    Spacer(modifier = Modifier.height(5.dp))

                    ListItemRow(description = stringResource(R.string.pm25), value = item.pm2_5String)
                    ListItemRow(description = stringResource(R.string.pm10), value = item.pm10String)
                    ListItemRow(description = stringResource(R.string.no2), value = item.no2)
                    ListItemRow(description = stringResource(R.string.o3), value = item.o3)
                }
            }
        },
        icon = {
            Image(painter = painterResource(R.drawable.circle_24px),
                contentDescription = stringResource(R.string.air_quality_icon),
                colorFilter = ColorFilter.tint(visualIndex(item.aqi)/*.copy(alpha = 0.5f)*/),
                modifier = Modifier
                    .size(10.dp)
                    .clickable(indication = null, interactionSource = interactionSource) { onClick() })
        },
        trailing = {
            ListItemArrow(expanded = expanded, onClick = { onClick() })
        }
    )
}

fun visualIndex(aqi: Int): Color {
    return when (aqi) {
        1 -> Color(0xFF2E7D32)  // Green800
        2 -> Color(0xFF4CAF50)  // MaterialGreen500
        3 -> Color(0xFFFF9800)  // MaterialOrange500
        4 -> Color(0xFFF44336)  // MaterialRed500
        5 -> Color.Black
        else -> Color(0xFF9E9E9E) // MaterialGrey500
    }
}

@Composable
private fun ListItemArrow(expanded: Boolean, onClick: () -> Unit = {}) {
    if (expanded) {
        IconButton(onClick = { onClick() }) {
            Icon(Icons.Filled.KeyboardArrowUp, contentDescription = stringResource(R.string.arrow_up))
        }
    } else {
        IconButton(onClick = { onClick() }) {
            Icon(Icons.Filled.KeyboardArrowDown, contentDescription = stringResource(R.string.arrow_down))
        }
    }
}

@Composable
fun ListItemRow(description: String, value: String) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(text = description, style = MaterialTheme.typography.body2)
        Text(text = value, style = MaterialTheme.typography.body2, modifier = Modifier.padding(start = 10.dp))
    }
}

val exampleCards = listOf(
    UiAirPollutionForecast(
        dt = "12:23",
        no2 = "23.31",
        o3 = "0.08",
        pm2_5 = 40.52,
        pm2_5String = "40.52",
        pm10 = 49.45,
        pm10String = "49.45",
        aqi = 2
    ),
    UiAirPollutionForecast(
        dt = "11:00",
        no2 = "23.99",
        o3 = "0.13",
        pm2_5 = 39.76,
        pm2_5String = "39.76",
        pm10 = 47.98,
        pm10String = "47.98",
        aqi = 4
    )
)

@Composable
@Preview
fun HourPollutionForecastPreview() {
    HourPollutionForecast(exampleCards)
}

@Composable
@Preview
fun HourForecastItemPreview() {
    HourForecastItem(exampleCards.first())
}

