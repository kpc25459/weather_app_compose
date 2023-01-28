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
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.datetime.LocalDateTime
import net.dev.weather.data.AirPollutionForecast
import kotlin.math.roundToInt

@Composable
fun HourPollutionForecast(forecast: List<AirPollutionForecast>) {
    LazyColumn {
        items(forecast) { item ->
            HourForecastItem(item)
            Spacer(modifier = Modifier.height(5.dp))
            Divider()
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HourForecastItem(item: AirPollutionForecast, modifier: Modifier = Modifier) {

    var expanded by rememberSaveable { mutableStateOf(false) }

    val onClick = { expanded = !expanded }

    val interactionSource = remember { MutableInteractionSource() }

    ListItem(
        text = { Text(text = item.dt.time.toString(), Modifier.clickable(indication = null, interactionSource = interactionSource) { onClick() }) },
        secondaryText = {
            Column {
                Text(text = "Jakość powietrza: ${item.airQuality}", Modifier.clickable(indication = null, interactionSource = interactionSource) { onClick() })

                if (expanded) {
                    Spacer(modifier = Modifier.height(5.dp))

                    ListItemRow(description = "PM 2.5", value = "${item.pm2_5.roundToInt()} µg/m3")
                    ListItemRow(description = "PM 10", value = "${item.pm10.roundToInt()} µg/m3")
                    ListItemRow(description = "NO2", value = item.no2.roundToInt().toString())
                    ListItemRow(description = "O3", value = item.o3.roundToInt().toString())
                }
            }
        },
        icon = {
            Image(imageVector = Icons.Filled.CheckCircle,
                contentDescription = "Air quality icon",
                colorFilter = ColorFilter.tint(visualIndex(item.aqi).copy(alpha = 0.5f)),
                modifier = Modifier
                    .size(30.dp)
                    .clickable(indication = null, interactionSource = interactionSource) { onClick() })
        },
        trailing = {
            ListItemArrow(expanded = expanded, onClick = { onClick() })
        }
    )
}

fun visualIndex(aqi: Int): Color {
    return when (aqi) {
        1 -> Color.Green
        2 -> Color.Green
        3 -> Color.Magenta
        4 -> Color.Red
        5 -> Color.Black
        else -> Color.Gray
    }
}

@Composable
private fun ListItemArrow(expanded: Boolean, onClick: () -> Unit = {}) {
    if (expanded) {
        IconButton(onClick = { onClick() }) {
            Icon(Icons.Filled.KeyboardArrowUp, contentDescription = "Arrow up")
        }
    } else {
        IconButton(onClick = { onClick() }) {
            Icon(Icons.Filled.KeyboardArrowDown, contentDescription = "Arrow down")
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
        Text(text = description)
        Text(text = value, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 10.dp))
    }
}


val exampleCards = listOf(
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
        nh3 = 1.5,
        aqi = 2
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
        nh3 = 1.38,
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

