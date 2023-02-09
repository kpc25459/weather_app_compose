package net.dev.weather.ui.weatherForecast

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.datetime.LocalDateTime
import net.dev.weather.R
import net.dev.weather.components.WeatherIcon
import net.dev.weather.data.WeatherDaily
import net.dev.weather.localDate

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DayForecastItem(weatherDaily: WeatherDaily, modifier: Modifier = Modifier) {

    var expanded by rememberSaveable { mutableStateOf(false) }

    val onClick = { expanded = !expanded }

    val interactionSource = remember { MutableInteractionSource() }

    ListItem(
        text = { Text(text = localDate(weatherDaily.dt), Modifier.clickable(indication = null, interactionSource = interactionSource) { onClick() }) },
        secondaryText = {
            Column {
                Text(text = weatherDaily.description, Modifier.clickable(indication = null, interactionSource = interactionSource) { onClick() })

                if (expanded) {
                    Spacer(modifier = Modifier.height(5.dp))

                    //TODO: powtórzenie kodu w innym pliku
                    ListItemRow(description = stringResource(R.string.temperature), value = weatherDaily.temp)
                    ListItemRow(description = stringResource(R.string.sunrise), value = weatherDaily.sunrise)
                    ListItemRow(description = stringResource(R.string.sunset), value = weatherDaily.sunset)
                    ListItemRow(description = stringResource(R.string.pressure), value = weatherDaily.pressure)
                    ListItemRow(description = stringResource(R.string.humidity), value = weatherDaily.humidity)
                    ListItemRow(description = stringResource(R.string.wind), value = weatherDaily.wind)
                    ListItemRow(description = stringResource(R.string.rain), value = weatherDaily.rain)
                    ListItemRow(description = stringResource(R.string.uv_Index), value = weatherDaily.uvi)
                }
            }
        },
        icon = {
            WeatherIcon(weatherDaily.icon, onClick = onClick)
        },
        trailing = {
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


val exampleCard = WeatherDaily(
    dt = LocalDateTime(2021, 5, 1, 0, 0),
    humidity = "84 %",
    pressure = "1014 hPa",
    sunrise = "08:02",
    sunset = "15:48",
    temp = "14°C / 7°C",
    uvi = "1",
    rain = "0 mm",
    description = "słabe opady deszczu",
    wind = "32 km/h SW",
    icon = "10d"
)

@Composable
@Preview()
fun DayForecastItemPreview() {
    DayForecastItem(exampleCard)
}
