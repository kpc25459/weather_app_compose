package net.dev.weather.ui.current_weather

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun Box() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        elevation = 8.dp,
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "Prognoza na: 2022-11-15")
                Text(text = "Godz. 21:14")
            }
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(imageVector = Icons.Filled.Place, contentDescription = "Place")
                    Text(text = "Komorniki, PL")
                }
                Text(text = "6°")
            }
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Jakość powietrza")
                Text(text = "Bardzo zła")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BoxPreview() {
    Box()
}
