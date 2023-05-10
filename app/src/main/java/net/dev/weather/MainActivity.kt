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
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import net.dev.weather.data.LatandLong
import net.dev.weather.theme.WeatherTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var locationCallback: LocationCallback? = null
    var fusedLocationClient: FusedLocationProviderClient? = null
    private var locationRequired = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherTheme {

                val context = LocalContext.current

                var currentLocation by remember {
                    mutableStateOf(LatandLong(0.toDouble(), 0.toDouble()))
                }

                val coroutineScope = rememberCoroutineScope()

                fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

                locationCallback = object : LocationCallback() {
                    override fun onLocationResult(p0: LocationResult) {
                        for (lo in p0.locations) {
                            // Update UI with location data
                            currentLocation = LatandLong(lo.latitude, lo.longitude)

                            //Log.i("MainActivity", "onLocationResult: ${lo.latitude}, ${lo.longitude}")

                            coroutineScope.launch {
                                context.settingsDataStore.updateData { currentSettings ->
                                    val location: Location = Location.newBuilder().setLatitude(lo.latitude).setLongitude(lo.longitude).build()

                                    currentSettings.toBuilder().setCurrentLocation(location).build()
                                }
                            }
                        }
                    }
                }

                val launcherMultiplePermissions = rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissionsMap ->
                    val areGranted = permissionsMap.values.reduce { acc, next -> acc && next }
                    if (areGranted) {
                        locationRequired = true
                        startLocationUpdates()
                        Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
                    }
                }

                val permissions = arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
                if (permissions.all { ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED }) {
                    startLocationUpdates()
                } else {
                    launcherMultiplePermissions.launch(permissions)
                }

                WeatherApp()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        locationCallback?.let {
            val locationRequest: LocationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000).build()
            fusedLocationClient?.requestLocationUpdates(locationRequest, it, Looper.getMainLooper())
        }
    }

    override fun onResume() {
        super.onResume()
        if (locationRequired) {
            startLocationUpdates()
        }
    }

    override fun onPause() {
        super.onPause()
        locationCallback?.let { fusedLocationClient?.removeLocationUpdates(it) }
    }
}
