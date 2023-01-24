package net.dev.weather.ui.airQuality

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Place
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import net.dev.weather.api.WeatherServiceApi
import net.dev.weather.components.ErrorScreen
import net.dev.weather.components.LoadingScreen
import net.dev.weather.components.WeatherIcon
import net.dev.weather.data.NetworkRepository
import net.dev.weather.data.WeatherHourly
import kotlin.math.roundToInt

@Composable
fun AirQualityScreen(modifier: Modifier = Modifier, viewModel: AirQualityViewModel = AirQualityViewModel(NetworkRepository(WeatherServiceApi.create()))) {

    val lifecycle = LocalLifecycleOwner.current.lifecycle

    val uiState by produceState<AirQualityUiState>(
        initialValue = AirQualityUiState.Loading,
        key1 = lifecycle,
        key2 = viewModel
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            viewModel.uiState.collect { value = it }
        }
    }

    if (uiState is AirQualityUiState.Loading) {
        LoadingScreen()
    }

    if (uiState is AirQualityUiState.Success) {
        AirQualityScreen(
            data = (uiState as AirQualityUiState.Success).data,
            modifier = modifier
        )
    }

    if (uiState is AirQualityUiState.Error) {
        ErrorScreen()
    }
}

@Composable
internal fun AirQualityScreen(data: String, modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .padding(5.dp)
            .verticalScroll(rememberScrollState())
    ) {
                                            Text(
                                                text = "Air quality :$data",
                                                //style = MaterialTheme.typography.h5,
                                                modifier = Modifier.padding(5.dp)
                                            )
    }

}

@Preview(showBackground = true)
@Composable
fun AirQualityScreenPreview() {
    AirQualityScreen()
}

