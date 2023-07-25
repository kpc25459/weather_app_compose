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
import net.dev.weather.R
import net.dev.weather.data.model.AirPollutionForecast
import net.dev.weather.data.model.airPollutionForecastPreviewItems
import net.dev.weather.utils.fromAqiIndex
import kotlin.math.roundToInt

@Composable
fun HourPollutionForecast(forecast: List<AirPollutionForecast>) {
    LazyColumn(modifier = Modifier.height(320.dp)) {
        items(forecast) { item ->
            HourForecastItem(item)
            Spacer(modifier = Modifier.height(5.dp))
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
        text = { Text(text = item.dt.time.toString(), style = MaterialTheme.typography.body1, modifier = Modifier.clickable(indication = null, interactionSource = interactionSource) { onClick() }) },
        secondaryText = {
            Column {
                Text(
                    text = stringResource(R.string.air_quality_with_value, fromAqiIndex(item.aqi)),
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.clickable(indication = null, interactionSource = interactionSource) { onClick() })

                if (expanded) {
                    Spacer(modifier = Modifier.height(5.dp))

                    ListItemRow(description = stringResource(R.string.pm25), value = "${item.pm2_5.roundToInt()} μg/m3")
                    ListItemRow(description = stringResource(R.string.pm10), value = "${item.pm10.roundToInt()} μg/m3")
                    ListItemRow(description = stringResource(R.string.no2), value = item.no2.roundToInt().toString())
                    ListItemRow(description = stringResource(R.string.o3), value = item.o3.roundToInt().toString())
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

@Composable
@Preview
fun HourPollutionForecastPreview() {
    HourPollutionForecast(airPollutionForecastPreviewItems)
}

@Composable
@Preview
fun HourForecastItemPreview() {
    HourForecastItem(airPollutionForecastPreviewItems.first())
}

