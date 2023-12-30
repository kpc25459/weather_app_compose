package kotlin.net.dev.weather

import androidx.compose.runtime.Composable
import net.dev.weather.theme.WeatherTheme
import net.dev.weather.ui.WeatherApp

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