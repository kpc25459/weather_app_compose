package kotlin.net.dev.weather

import androidx.compose.runtime.Composable
import kotlinx.coroutines.launch
import net.dev.weather.MainActivityUiState
import net.dev.weather.components.LocationUpdatesEffect
import net.dev.weather.components.PermissionBox
import net.dev.weather.data.model.LatandLong
import net.dev.weather.data.model.PlaceMode
import net.dev.weather.theme.WeatherTheme
import net.dev.weather.ui.WeatherApp
import net.dev.weather.ui.places.permissions

@Composable
fun App() {
    WeatherTheme(useDarkTheme = /*darkTheme*/ false) {
        WeatherApp(windowSizeClass = calculateWindowSizeClass(activity))
       /* when (uiState) {
            MainActivityUiState.Loading -> {
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
        }*/
    }
}