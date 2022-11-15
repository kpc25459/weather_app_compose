package net.dev.weather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.dev.weather.ui.current_weather.Box
import net.dev.weather.ui.hour_forecast.HourForecast
import net.dev.weather.ui.theme.WeatherTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    MainPage()
                }
            }
        }
    }
}

@Composable
fun MainPage() {
    Column {
        Box()
        Spacer(modifier = Modifier.height(20.dp))
        HourForecast()
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    WeatherTheme {
        MainPage()
    }
}