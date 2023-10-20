package net.dev.weather

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.Priority
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import net.dev.weather.MainActivityUiState.Loading
import net.dev.weather.components.LocationUpdatesEffect
import net.dev.weather.components.PermissionBox
import net.dev.weather.data.model.DarkThemeConfig
import net.dev.weather.data.model.LatandLong
import net.dev.weather.data.model.PlaceMode
import net.dev.weather.theme.WeatherTheme
import net.dev.weather.ui.WeatherApp
import net.dev.weather.ui.places.permissions

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var uiState: MainActivityUiState by mutableStateOf(Loading)

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState
                    .onEach {
                        uiState = it
                    }
                    .collect()
            }
        }

        setContent {

            val systemUiController = rememberSystemUiController()
            val darkTheme = shouldUseDarkTheme(uiState)

            val coroutineScope = rememberCoroutineScope()

            DisposableEffect(systemUiController, darkTheme) {
                systemUiController.systemBarsDarkContentEnabled = !darkTheme
                onDispose { }
            }

            Content(this, coroutineScope, darkTheme, uiState)
        }
    }

    @SuppressLint("MissingPermission")
    @Composable
    private fun Content(activity: Activity, coroutineScope: CoroutineScope, darkTheme: Boolean, uiState: MainActivityUiState) {
        var locationRequest by remember {
            mutableStateOf<LocationRequest?>(null)
        }

        WeatherTheme(useDarkTheme = darkTheme) {

            when (uiState) {
                Loading -> {
                    Text(
                        text = "Loading..."
                    )
                }

                is MainActivityUiState.Success -> {
                    if (uiState.userData.currentMode == PlaceMode.DEVICE_LOCATION) {
                        LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000).build().also {
                            locationRequest = it

                            PermissionBox(
                                permissions = permissions,
                                requiredPermissions = listOf(permissions.first())
                            ) {

                                LocationUpdatesEffect(locationRequest!!) { result ->
                                    for (currentLocation in result.locations) {
                                        Log.i("MainActivity", "LocationUpdatesEffect: $currentLocation")

                                        coroutineScope.launch {
                                            viewModel.setCurrentLocation(LatandLong(currentLocation.latitude, currentLocation.longitude))
                                        }
                                    }
                                }

                                WeatherApp(
                                    windowSizeClass = calculateWindowSizeClass(activity),
                                )
                            }
                        }
                    } else {
                        locationRequest = null

                        WeatherApp(windowSizeClass = calculateWindowSizeClass(activity))
                    }
                }
            }
        }
    }

    @Composable
    private fun shouldUseDarkTheme(uiState: MainActivityUiState): Boolean = when (uiState) {
        Loading -> isSystemInDarkTheme()
        is MainActivityUiState.Success -> when (uiState.userData.darkThemeConfig) {
            DarkThemeConfig.FOLLOW_SYSTEM -> isSystemInDarkTheme()
            DarkThemeConfig.LIGHT -> false
            DarkThemeConfig.DARK -> true
        }
    }
}
