package net.dev.weather

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.gms.location.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import net.dev.weather.MainActivityUiState.Loading
import net.dev.weather.data.model.DarkThemeConfig
import net.dev.weather.data.model.LatandLong
import net.dev.weather.data.model.PlaceMode
import net.dev.weather.theme.WeatherTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var locationCallback: LocationCallback? = null
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var locationRequired = false

    val viewModel: MainActivityViewModel by viewModels()

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

            //val systemUiController = rememberSystemUiController()
            val darkTheme = shouldUseDarkTheme(uiState)

            //TODO: update system bars theme

            WeatherTheme(darkTheme = darkTheme) {

                when (uiState) {
                    Loading -> {
                        //TODO: dodać splash screen/kręciołek
                    }

                    is MainActivityUiState.Success -> {
                        if ((uiState as MainActivityUiState.Success).userData.currentMode == PlaceMode.DEVICE_LOCATION) {
                            StartLocationUpdate()
                        } else {
                            stopLocationUpdates()
                        }
                        WeatherApp()
                    }
                }

            }
        }
    }

    @Composable
    private fun StartLocationUpdate() {

        val context = LocalContext.current

        var currentLocation by remember {
            mutableStateOf(LatandLong(0.toDouble(), 0.toDouble()))
        }

        val coroutineScope = rememberCoroutineScope()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                for (lo in p0.locations) {
                    currentLocation = LatandLong(lo.latitude, lo.longitude)

                    Log.d("MainActivity", "setCurrentLocation: ${lo.latitude}, ${lo.longitude}")

                    coroutineScope.launch {
                        viewModel.setCurrentLocation(LatandLong(lo.latitude, lo.longitude))
                    }
                }
            }
        }

        if (permissions.all { ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED }) {
            startLocationUpdates()
        } else {
            val permissionsLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissionsMap ->
                val areGranted = permissionsMap.values.reduce { acc, next -> acc && next }
                if (areGranted) {
                    locationRequired = true
                    startLocationUpdates()
                } else {
                    Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
            }

            SideEffect {
                permissionsLauncher.launch(permissions)
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        Log.i("MainActivity", "startLocationUpdates")

        locationCallback?.let {
            val locationRequest: LocationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000).build()
            fusedLocationClient?.requestLocationUpdates(locationRequest, it, Looper.getMainLooper())
        }
    }

    private fun stopLocationUpdates() {
        Log.i("MainActivity", "stopLocationUpdates")
        locationCallback?.let { fusedLocationClient?.removeLocationUpdates(it) }
    }

    override fun onResume() {
        super.onResume()
        if (locationRequired) {
            startLocationUpdates()
        }
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
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

    companion object {
        val permissions = arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }
}
