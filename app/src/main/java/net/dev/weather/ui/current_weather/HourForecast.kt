package net.dev.weather.ui.current_weather

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun HourForecast() {

    val scrollState = rememberScrollState()

    Row(Modifier.horizontalScroll(scrollState)) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(10.dp)) {
            Text(text = "Czw.")
            Icon(imageVector = Icons.Filled.Close, contentDescription = null)
            Text(text = "20:00")
            Text(text = "1°")
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(10.dp)) {
            Text(text = "Czw.")
            Icon(imageVector = Icons.Filled.Close, contentDescription = null)
            Text(text = "21:00")
            Text(text = "1°")
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(10.dp)) {
            Text(text = "Czw.")
            Icon(imageVector = Icons.Filled.Close, contentDescription = null)
            Text(text = "22:00")
            Text(text = "1°")
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(10.dp)) {
            Text(text = "Czw.")
            Icon(imageVector = Icons.Filled.Close, contentDescription = null)
            Text(text = "23:00")
            Text(text = "1°")
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(10.dp)) {
            Text(text = "Czw.")
            Icon(imageVector = Icons.Filled.Close, contentDescription = null)
            Text(text = "23:00")
            Text(text = "1°")
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(10.dp)) {
            Text(text = "Pt.")
            Icon(imageVector = Icons.Filled.Close, contentDescription = null)
            Text(text = "00:00")
            Text(text = "1°")
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(10.dp)) {
            Text(text = "Pt.")
            Icon(imageVector = Icons.Filled.Close, contentDescription = null)
            Text(text = "01:00")
            Text(text = "1°")
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(10.dp)) {
            Text(text = "Pt.")
            Icon(imageVector = Icons.Filled.Close, contentDescription = null)
            Text(text = "02:00")
            Text(text = "1°")
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(10.dp)) {
            Text(text = "Pt.")
            Icon(imageVector = Icons.Filled.Close, contentDescription = null)
            Text(text = "03:00")
            Text(text = "1°")
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(10.dp)) {
            Text(text = "Pt.")
            Icon(imageVector = Icons.Filled.Close, contentDescription = null)
            Text(text = "04:00")
            Text(text = "1°")
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(10.dp)) {
            Text(text = "Pt.")
            Icon(imageVector = Icons.Filled.Close, contentDescription = null)
            Text(text = "05:00")
            Text(text = "1°")
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(10.dp)) {
            Text(text = "Pt.")
            Icon(imageVector = Icons.Filled.Close, contentDescription = null)
            Text(text = "06:00")
            Text(text = "1°")
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(10.dp)) {
            Text(text = "Pt.")
            Icon(imageVector = Icons.Filled.Close, contentDescription = null)
            Text(text = "07:00")
            Text(text = "1°")
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(10.dp)) {
            Text(text = "Pt.")
            Icon(imageVector = Icons.Filled.Close, contentDescription = null)
            Text(text = "08:00")
            Text(text = "1°")
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(10.dp)) {
            Text(text = "Pt.")
            Icon(imageVector = Icons.Filled.Close, contentDescription = null)
            Text(text = "09:00")
            Text(text = "1°")
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(10.dp)) {
            Text(text = "Pt.")
            Icon(imageVector = Icons.Filled.Close, contentDescription = null)
            Text(text = "10:00")
            Text(text = "1°")
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(10.dp)) {
            Text(text = "Pt.")
            Icon(imageVector = Icons.Filled.Close, contentDescription = null)
            Text(text = "11:00")
            Text(text = "1°")
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(10.dp)) {
            Text(text = "Pt.")
            Icon(imageVector = Icons.Filled.Close, contentDescription = null)
            Text(text = "12:00")
            Text(text = "1°")
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(10.dp)) {
            Text(text = "Pt.")
            Icon(imageVector = Icons.Filled.Close, contentDescription = null)
            Text(text = "13:00")
            Text(text = "1°")
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(10.dp)) {
            Text(text = "Pt.")
            Icon(imageVector = Icons.Filled.Close, contentDescription = null)
            Text(text = "14:00")
            Text(text = "1°")
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(10.dp)) {
            Text(text = "Pt.")
            Icon(imageVector = Icons.Filled.Close, contentDescription = null)
            Text(text = "15:00")
            Text(text = "1°")
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(10.dp)) {
            Text(text = "Pt.")
            Icon(imageVector = Icons.Filled.Close, contentDescription = null)
            Text(text = "16:00")
            Text(text = "1°")
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(10.dp)) {
            Text(text = "Pt.")
            Icon(imageVector = Icons.Filled.Close, contentDescription = null)
            Text(text = "17:00")
            Text(text = "1°")
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(10.dp)) {
            Text(text = "Pt.")
            Icon(imageVector = Icons.Filled.Close, contentDescription = null)
            Text(text = "18:00")
            Text(text = "1°")
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(10.dp)) {
            Text(text = "Pt.")
            Icon(imageVector = Icons.Filled.Close, contentDescription = null)
            Text(text = "19:00")
            Text(text = "1°")
        }

    }


}

@Preview(showBackground = true)
@Composable
fun HourForecastPreview() {
    HourForecast()
}
