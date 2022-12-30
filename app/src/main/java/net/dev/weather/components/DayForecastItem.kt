package net.dev.weather.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.datetime.LocalDateTime
import net.dev.weather.data.ExpandableCardModel
import net.dev.weather.data.WeatherDaily
import net.dev.weather.localDate

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DayForecastItem(card: ExpandableCardModel, expanded: Boolean) {

    val item = card.weatherDaily

    //TODO: tutaj przerobić nazwy miesiąca na ładne polskie
    ListItem(
        text = { Text(text = localDate(item.dt)) },
        secondaryText = {
            Column {
                Text(text = item.description)

                if (expanded) {
                    Spacer(modifier = Modifier.height(5.dp))
                    ForecastRow(description = "Temperatura", value = "${item.temp}°C")
                    ForecastRow(description = "Wschód słońca", value = item.sunrise)
                    ForecastRow(description = "Zachód słońca", value = item.sunset)
                    ForecastRow(description = "Ciśnienie", value = item.pressure)
                    ForecastRow(description = "Wilgotność", value = item.humidity)
                    ForecastRow(description = "Wiatr", value = item.wind)
                    ForecastRow(description = "Deszcz", value = item.rain)
                    ForecastRow(description = "Indeks Uv", value = item.uvi)
                }
            }
        },
        icon = { Icon(Icons.Filled.Favorite, contentDescription = null) },
        trailing = {
            if (expanded) {
                Icon(Icons.Filled.KeyboardArrowUp, contentDescription = "Arrow up")
            } else {
                Icon(Icons.Filled.KeyboardArrowDown, contentDescription = "Arrow down")
            }
        }
    )
}

@Composable
fun ForecastRow(description: String, value: String) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = description)
        Text(text = value)
    }
}


val exampleCard = ExpandableCardModel(
    id = 1,
    weatherDaily = WeatherDaily(
        dt = LocalDateTime(2021, 5, 1, 0, 0),
        humidity = "84 %",
        pressure = "1014 hPa",
        sunrise = "08:02",
        sunset = "15:48",
        temp = "14°C / 7°C",
        uvi = "1",
        rain = "0 mm",
        description = "słabe opady deszczu",
        wind = "32 km/h SW"
    )
)

@Composable
@Preview()
fun DayForecastItemPreview() {
    DayForecastItem(exampleCard, false)
}

@Composable
@Preview()
fun DayForecastItemExpandedPreview() {
    DayForecastItem(exampleCard, true)
}