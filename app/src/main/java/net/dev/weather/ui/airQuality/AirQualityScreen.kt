package net.dev.weather.ui.airQuality

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.himanshoe.charty.common.axis.AxisConfig
import com.himanshoe.charty.common.dimens.ChartDimens
import com.himanshoe.charty.line.CurveLineChart
import com.himanshoe.charty.line.LineChart
import com.himanshoe.charty.line.config.CurveLineConfig
import com.himanshoe.charty.line.config.LineConfig
import com.himanshoe.charty.line.model.LineData
import net.dev.weather.R
import net.dev.weather.bottomNavigationBar
import net.dev.weather.components.WeatherTopAppBar
import net.dev.weather.data.model.AirPollutionForecast
import net.dev.weather.sampleAirQuality
import net.dev.weather.ui.model.PlaceWithAirPollutionForecast
import net.dev.weather.utils.fromAqiIndex
import net.dev.weather.utils.imageFromAqi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AirQualityScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: AirQualityViewModel = hiltViewModel(),
) {
    Scaffold(
        topBar = { WeatherTopAppBar(R.string.air_quality_screen_title) },
        bottomBar = bottomNavigationBar(navController = navController),
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->

        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        uiState.airQuality?.let { main ->
            Content(main, modifier = Modifier.padding(paddingValues))
        }
    }
}

@Composable
private fun Content(data: PlaceWithAirPollutionForecast, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .padding(5.dp)
            .verticalScroll(rememberScrollState())
    ) {
        CardBox(data.place.name, data.airPollutionForecast)
        Spacer(modifier = Modifier.height(20.dp))
        HourPollutionForecast(data.airPollutionForecast.take(4))

        val forecast5days = data.airPollutionForecast.take(24 * 5)
        Spacer(modifier = Modifier.height(20.dp))
        Chart(title = "Prognoza dla PM 2.5", data = forecast5days, transform = AirPollutionForecast::pm2_5)

        Spacer(modifier = Modifier.height(20.dp))
        Chart(title = "Prognoza dla PM 10", data = forecast5days, transform = AirPollutionForecast::pm10)
    }
}

@Composable
fun CardBox(location: String, data: List<AirPollutionForecast>) {
    val currentWeather = data.first()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(horizontal = 8.dp),
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopStart) {

            Image(
                painterResource(id = imageFromAqi(currentWeather.aqi)),
                contentDescription = stringResource(R.string.weather_condition),
                contentScale = ContentScale.Crop,
                colorFilter = ColorFilter.tint(Color.Black.copy(alpha = 0.5f), blendMode = BlendMode.SrcOver),
                modifier = Modifier
                    .fillMaxWidth()
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
                    .padding(10.dp)
                    .padding(top = 30.dp)
                    .fillMaxWidth()
            ) {
                Row {
                    Image(painter = painterResource(R.drawable.outline_location_on_24), contentDescription = stringResource(R.string.place), colorFilter = ColorFilter.tint(Color.White))
                    Text(text = location, color = Color.White)
                }

                Row {
                    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = fromAqiIndex(currentWeather.aqi), color = Color.White)
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(text = stringResource(R.string.pm25_extended, currentWeather.pm2_5), color = Color.White)
                    Text(text = stringResource(R.string.pm10_extended, currentWeather.pm10), color = Color.White)
                }
            }
        }


    }
}

@Composable
fun Chart(title: String, data: List<AirPollutionForecast>, transform: (AirPollutionForecast) -> Double) {
    Column(modifier = Modifier.padding(10.dp)) {
        Text(text = title)

        //NOTE: nie wyświetlają się labele na osi X bo jest ich za dużo - nie ma możliwości konfiguracji tego w zewnętrznej bibliotece

        //LineChart(data, transform)
        CurveLineChart(data, transform)
    }
}

/*
 *  Wykres liniowy
 *  Nie ma tła pod linią, ale nie wyświetla zerowych wartości na początku i końcu
 */
@Composable
private fun LineChart(data: List<AirPollutionForecast>, transform: (AirPollutionForecast) -> Double) {
    LineChart(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
            .height(100.dp),
        color = /*iconColor*/Color.Black,
        lineConfig = LineConfig(
            hasSmoothCurve = true,
            hasDotMarker = false,
        ),
        lineData = data.map {
            LineData(it.dt, transform(it).toFloat())
        },
        chartDimens = ChartDimens(4.dp),
        axisConfig = AxisConfig(
            xAxisColor = Color.LightGray,
            showAxis = true,
            isAxisDashed = true,
            showUnitLabels = true,
            showXLabels = true,
            yAxisColor = Color.LightGray,
            textColor = Color.Black,
        )
    )
}

/*
 * Wykres krzywej
 * Jest tło pod linią, ale zerowe wartości są wyświetlane na początku i końcu
 */
@Composable
private fun CurveLineChart(data: List<AirPollutionForecast>, transform: (AirPollutionForecast) -> Double) {
    CurveLineChart(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
            .height(100.dp),
        chartColor = MaterialTheme.colorScheme.primaryContainer,
        lineColor = MaterialTheme.colorScheme.onPrimaryContainer,
        curveLineConfig = CurveLineConfig(
            hasDotMarker = false,
        ),
        lineData = data.map {
            LineData(it.dt, transform(it).toFloat())
        },
        chartDimens = ChartDimens(4.dp),
        axisConfig = AxisConfig(
            xAxisColor = MaterialTheme.colorScheme.onPrimaryContainer,
            showAxis = true,
            isAxisDashed = true,
            showUnitLabels = true,
            showXLabels = true,
            yAxisColor = MaterialTheme.colorScheme.onPrimaryContainer,
            textColor = MaterialTheme.colorScheme.onPrimaryContainer,
        )
    )
}

@Composable
@Preview()
fun ContentPreview() {
    Content(data = sampleAirQuality)
}

@Composable
@Preview()
fun BoxPreview() {
    CardBox(data = sampleAirQuality.airPollutionForecast, location = sampleAirQuality.place.name)
}


@Composable
@Preview()
fun ChartPreview() {
    Chart(title = "Prognoza dla PM 2.5", data = sampleAirQuality.airPollutionForecast.take(24 * 5), transform = { it.pm2_5 })
}
