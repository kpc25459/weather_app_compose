package net.dev.weather.ui.airQuality

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.himanshoe.charty.common.axis.AxisConfig
import com.himanshoe.charty.common.dimens.ChartDimens
import com.himanshoe.charty.line.CurveLineChart
import com.himanshoe.charty.line.LineChart
import com.himanshoe.charty.line.config.CurveLineConfig
import com.himanshoe.charty.line.config.LineConfig
import com.himanshoe.charty.line.model.LineData
import net.dev.weather.*
import net.dev.weather.R
import net.dev.weather.api.WeatherServiceApi
import net.dev.weather.data.AirQuality
import net.dev.weather.data.NetworkRepository
import net.dev.weather.theme.iconColor
import net.dev.weather.theme.tabBarBackgroundColor
import net.dev.weather.theme.tabBarTextColor
import net.dev.weather.ui.model.UiAirPollutionForecast
import net.dev.weather.utils.fromAqiIndex
import net.dev.weather.utils.imageFromAqi

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun AirQualityScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: AirQualityViewModel = AirQualityViewModel(NetworkRepository(WeatherServiceApi.create())),
    scaffoldState: ScaffoldState = rememberScaffoldState()
) {
    Scaffold(
        topBar = topBar(),
        bottomBar = bottomNavigationBar(navController = navController),
        scaffoldState = scaffoldState,
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->

        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        uiState.airQuality?.let { main ->
            Content(main, modifier = Modifier.padding(paddingValues))
        }
    }
}


@Composable
private fun topBar(): @Composable () -> Unit {
    return {
        TopAppBar(
            title = { Text(text = stringResource(id = R.string.air_quality_screen_title)) },
            backgroundColor = tabBarBackgroundColor,
            contentColor = tabBarTextColor,
            elevation = 0.dp,
            modifier = Modifier.fillMaxWidth()
        )
    }
}


@Composable
private fun Content(data: AirQuality, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .padding(5.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Box(data.location, data.airPollutionForecast)
        Spacer(modifier = Modifier.height(20.dp))
        HourPollutionForecast(data.airPollutionForecast.take(4))

        val forecast5days = data.airPollutionForecast.take(24 * 5)
        Spacer(modifier = Modifier.height(20.dp))
        Chart(title = "Prognoza dla PM 2.5", data = forecast5days, transform = UiAirPollutionForecast::pm2_5)

        Spacer(modifier = Modifier.height(20.dp))
        Chart(title = "Prognoza dla PM 10", data = forecast5days, transform = UiAirPollutionForecast::pm10)
    }
}

@Composable
fun Box(location: String, data: List<UiAirPollutionForecast>) {
    val currentWeather = data.first()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(horizontal = 8.dp),
        elevation = 8.dp
    ) {
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

@Composable
fun Chart(title: String, data: List<UiAirPollutionForecast>, transform: (UiAirPollutionForecast) -> Double) {
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
private fun LineChart(data: List<UiAirPollutionForecast>, transform: (UiAirPollutionForecast) -> Double) {
    LineChart(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
            .height(100.dp),
        color = iconColor,
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
private fun CurveLineChart(data: List<UiAirPollutionForecast>, transform: (UiAirPollutionForecast) -> Double) {
    CurveLineChart(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
            .height(100.dp),
        chartColor = tabBarBackgroundColor,
        lineColor = iconColor,
        curveLineConfig = CurveLineConfig(
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
@Composable
@Preview()
fun ChartPreview() {
    Chart(title = "Prognoza dla PM 2.5", data = sampleMain.airPollutionForecast.take(24 * 5), transform = { it.pm2_5 })
}*/

/*@Preview(showBackground = true)
@Composable
fun AirQualityScreenPreview() {
    AirQualityScreen(data = sampleMain)
}*/

