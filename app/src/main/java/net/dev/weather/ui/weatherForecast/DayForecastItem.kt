package net.dev.weather.ui.weatherForecast

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.dev.weather.R
import net.dev.weather.components.WeatherIcon
import net.dev.weather.data.model.WeatherDaily
import net.dev.weather.sampleData
import net.dev.weather.utils.localDate
import kotlin.math.roundToInt

@Composable
fun DayForecastItem(weatherDaily: WeatherDaily, initiallyExpanded: Boolean = true, modifier: Modifier = Modifier) {

    var expanded by rememberSaveable { mutableStateOf(initiallyExpanded) }

    val onClick = { expanded = !expanded }

    val interactionSource = remember { MutableInteractionSource() }

    ListItem(
        headlineContent = { Text(text = localDate(weatherDaily.dt), Modifier.clickable(indication = null, interactionSource = interactionSource) { onClick() }) },

        supportingContent = {
            Column {
                Text(text = weatherDaily.description, modifier = Modifier.clickable(indication = null, interactionSource = interactionSource) { onClick() })

                if (expanded) {
                    Spacer(modifier = Modifier.height(5.dp))

                    ListItemRow(description = stringResource(R.string.temperature), value = "${weatherDaily.tempDay.roundToInt()}°C / ${weatherDaily.tempNight.roundToInt()}°C")
                    ListItemRow(description = stringResource(R.string.sunrise), value = weatherDaily.sunrise.toString().substringBeforeLast(":"))
                    ListItemRow(description = stringResource(R.string.sunset), value = weatherDaily.sunset.toString().substringBeforeLast(":"))
                    ListItemRow(description = stringResource(R.string.pressure), value = "${weatherDaily.pressure} hPa")
                    ListItemRow(description = stringResource(R.string.humidity), value = "${weatherDaily.humidity} %")
                    ListItemRow(description = stringResource(R.string.wind), value = "${weatherDaily.wind.roundToInt()} km/h ${weatherDaily.windDirection}")
                    ListItemRow(description = stringResource(R.string.rain), value = "${weatherDaily.rain.roundToInt()} mm/24h")
                    ListItemRow(description = stringResource(R.string.uv_Index), value = weatherDaily.uvi.roundToInt().toString())
                }
            }
        },
        leadingContent = {
            WeatherIcon(weatherDaily.icon, onClick = onClick)
        },
        trailingContent = {
            ListItemArrow(expanded = expanded, onClick = { onClick() })
        }
    )
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
        Text(text = description)
        Text(text = value, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 10.dp))
    }
}

@Composable
@Preview()
fun DayForecastItemPreview() {
    DayForecastItem(sampleData.weather.daily.first())
}
