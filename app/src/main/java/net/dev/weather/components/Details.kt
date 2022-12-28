package net.dev.weather.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.dev.weather.viewmodels.CurrentWeatherViewModel

@Composable
fun CurrentWeatherDetails(viewModel: CurrentWeatherViewModel) {

    val currentWeather by viewModel.currentWeather.observeAsState()

    currentWeather?.let { weather ->
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column {
                DetailsItem(
                    image = Icons.Filled.ArrowForward,
                    name = "Wschód słońca",
                    value = weather.sunrise
                )
                DetailsItem(
                    image = Icons.Filled.ArrowForward,
                    name = "Ciśnienie",
                    value = weather.pressure
                )
                DetailsItem(
                    image = Icons.Filled.ArrowForward,
                    name = "Indeks UV",
                    value = weather.uvi,
                )
                DetailsItem(
                    image = Icons.Filled.ArrowForward,
                    name = "Deszcz",
                    value = weather.rain,
                )
            }

            Column {
                DetailsItem(
                    image = Icons.Filled.ArrowForward,
                    name = "Zachód słońca",
                    value = weather.sunset
                )
                DetailsItem(
                    image = Icons.Filled.ArrowForward,
                    name = "Wilgotność",
                    value = weather.humidity
                )
                DetailsItem(
                    image = Icons.Filled.ArrowForward,
                    name = "Wiatr",
                    value = weather.wind
                )
                DetailsItem(
                    image = Icons.Filled.ArrowForward,
                    name = "Odczuwana",
                    value = weather.feels_like,
                )
            }
        }
    }
}

@Composable
fun DetailsItem(image: ImageVector, name: String, value: String) {
    Row(
        modifier = Modifier
            .padding(8.dp)
            //TODO; tutaj pozbyć się stałej szerokości
            .width(180.dp)
            .background(color = Color(0xFFE0EAFF), shape = RoundedCornerShape(10.dp))
    ) {
        Icon(imageVector = image, contentDescription = null, modifier = Modifier.padding(10.dp))
        Column(modifier = Modifier.padding(10.dp)) {
            Text(text = name)
            Text(text = value)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailsItemPreview() {
    DetailsItem(image = Icons.Filled.ArrowForward, name = "Wschód słońca", value = "06:00")
}

@Preview(showBackground = true)
@Composable
fun CurrentWeatherDetailsPreview() {
    /*Column {
        CurrentWeatherDetails(viewModel)
    }*/
}
