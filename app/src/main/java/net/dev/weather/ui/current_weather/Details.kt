package net.dev.weather.ui.current_weather

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CurrentWeatherDetails() {

    Row {
        Column {
            MyItem(
                image = Icons.Filled.ArrowForward,
                name = "Wschód słońca",
                value = "07:17",
            )
            MyItem(
                image = Icons.Filled.ArrowForward,
                name = "Ciśnienie",
                value = "1003 hPa",
            )
            MyItem(
                image = Icons.Filled.ArrowForward,
                name = "Indeks UV",
                value = "0",
            )
            MyItem(
                image = Icons.Filled.ArrowForward,
                name = "Deszcz",
                value = "0 mm/24h",
            )
        }

        Column {
            MyItem(
                image = Icons.Filled.ArrowForward,
                name = "Zachód słońca",
                value = "07:17",
            )
            MyItem(
                image = Icons.Filled.ArrowForward,
                name = "Wilgotność",
                value = "69 %",
            )
            MyItem(
                image = Icons.Filled.ArrowForward,
                name = "Wiatr",
                value = "22 km/h E",
            )
            MyItem(
                image = Icons.Filled.ArrowForward,
                name = "Odczuwana",
                value = "-5 °C",
            )
        }
    }
}

@Composable
fun MyItem(image: ImageVector, name: String, value: String) {
    Row(
        modifier = Modifier
            .padding(10.dp)
    ) {
        Icon(imageVector = image, contentDescription = null)
        Column(modifier = Modifier.padding(start = 10.dp)) {
            Text(text = name)
            Text(text = value)
        }
    }

}

@Preview(showBackground = true)
@Composable
fun CurrentWeatherDetailsPreview() {
    Column {
        CurrentWeatherDetails()
    }
}
