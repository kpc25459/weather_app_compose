package net.dev.weather.ui.airQuality

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import com.himanshoe.charty.common.axis.AxisConfig
import com.himanshoe.charty.common.dimens.ChartDimens
import com.himanshoe.charty.line.CurveLineChart
import com.himanshoe.charty.line.LineChart
import com.himanshoe.charty.line.config.CurveLineConfig
import com.himanshoe.charty.line.config.LineConfig
import com.himanshoe.charty.line.model.LineData
import net.dev.weather.R
import net.dev.weather.components.LoadingScreen
import net.dev.weather.data.model.AirPollutionForecast
import net.dev.weather.sampleAirQuality
import net.dev.weather.ui.model.PlaceWithAirPollutionForecast
import net.dev.weather.ui.weatherForecast.ExpandableListItem
import net.dev.weather.ui.weatherForecast.ListItemRow
import net.dev.weather.utils.fromAqiIndex
import net.dev.weather.utils.imageFromAqi
import kotlin.math.roundToInt

@Composable
fun AirQualityScreen(
    modifier: Modifier = Modifier,
    viewModel: AirQualityViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    if (uiState.isLoading) {
        LoadingScreen()
    } else {

        //TODO: obsłużyć brak danych (request poprawny, ale pusta lista)

        uiState.airQuality?.let { main ->
            Content(main, modifier = modifier)
        }
    }
}

@Composable
private fun Content(data: PlaceWithAirPollutionForecast, modifier: Modifier = Modifier) {
    val hourForecastItems: List<AirPollutionForecast> = data.airPollutionForecast.take(4)
    var expandedCardsIdxs by rememberSaveable { mutableStateOf<List<String>>(mutableListOf()) }

    val forecast5days = data.airPollutionForecast.take(24 * 5)

    LazyColumn(
        modifier = modifier
            .padding(5.dp)
    ) {

        item { CardBox(data.place.name, data.airPollutionForecast) }
        item { Spacer(modifier = Modifier.height(20.dp)) }

        itemsIndexed(hourForecastItems) { idx, item ->

            ExpandableListItem(
                expanded = expandedCardsIdxs.contains(idx.toString()),
                modifier = modifier,
                headlineContent = {
                    Text(text = item.dt.time.toString(),
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.clickable(/*indication = null, interactionSource = interactionSource*/) {
                            val i = idx.toString()

                            expandedCardsIdxs = if (expandedCardsIdxs.contains(i)) {
                                expandedCardsIdxs.filter { it != i }
                            } else {
                                expandedCardsIdxs + i
                            }
                        })
                },
                content = {
                    Text(
                        text = stringResource(R.string.air_quality_with_value, fromAqiIndex(item.aqi)),
                        style = MaterialTheme.typography.bodyMedium,
                        /*modifier = Modifier.clickable(indication = null, interactionSource = interactionSource) { onClick() }*/
                    )
                },
                leadingContent = {
                    Image(painter = painterResource(R.drawable.circle_24px),
                        contentDescription = stringResource(R.string.air_quality_icon),
                        colorFilter = ColorFilter.tint(visualIndex(item.aqi) /*.copy(alpha = 0.5f)*/),
                                modifier = Modifier
                            .minimumInteractiveComponentSize()
                            .size(16.dp)
                            /*.clickable(indication = null, interactionSource = interactionSource) { onClick() }*/)

                }) {
                ListItemRow(label = stringResource(R.string.pm25), value = "${item.pm2_5.roundToInt()} μg/m3")
                ListItemRow(label = stringResource(R.string.pm10), value = "${item.pm10.roundToInt()} μg/m3")
                ListItemRow(label = stringResource(R.string.no2), value = item.no2.roundToInt().toString())
                ListItemRow(label = stringResource(R.string.o3), value = item.o3.roundToInt().toString())

            }

            Spacer(modifier = Modifier.height(5.dp))
        }

        item { Spacer(modifier = Modifier.height(20.dp)) }
        item { Chart(title = "Prognoza dla PM 2.5", data = forecast5days, transform = AirPollutionForecast::pm2_5) }

        item { Spacer(modifier = Modifier.height(20.dp)) }
        item { Chart(title = "Prognoza dla PM 10", data = forecast5days, transform = AirPollutionForecast::pm10) }
    }
}

fun visualIndex(aqi: Int): Color {
    return when (aqi) {
        1 -> Color(0xFF2E7D32)  // Green800
        2 -> Color(0xFF4CAF50)  // MaterialGreen500
        3 -> Color(0xFFFF9800)  // MaterialOrange500
        4 -> Color(0xFFF44336)  // MaterialRed500
        5 -> Color.Black
        else -> Color(0xFF9E9E9E) // MaterialGrey500
    }
}


@Composable
fun CardBox(location: String, data: List<AirPollutionForecast>) {
    val currentWeather = data.firstOrNull() ?: return

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
                modifier = Modifier.fillMaxWidth()
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
                        .padding(top = 10.dp), horizontalArrangement = Arrangement.SpaceEvenly
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
            .height(100.dp), color = /*iconColor*/Color.Black, lineConfig = LineConfig(
            hasSmoothCurve = true,
            hasDotMarker = false,
        ), lineData = data.map {
            LineData(it.dt, transform(it).toFloat())
        }, chartDimens = ChartDimens(4.dp), axisConfig = AxisConfig(
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
