package net.dev.weather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import net.dev.weather.api.WeatherServiceApi
import net.dev.weather.data.NetworkRepository
import net.dev.weather.theme.*

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel = MainViewModel(NetworkRepository(WeatherServiceApi.create()))

    @OptIn(ExperimentalLifecycleComposeApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherTheme {
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                WeatherApp(uiState = uiState)
            }
        }
    }
}
