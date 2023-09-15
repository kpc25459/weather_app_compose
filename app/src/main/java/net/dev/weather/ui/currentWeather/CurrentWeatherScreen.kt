package net.dev.weather.ui.currentWeather

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import net.dev.weather.R
import net.dev.weather.bottomNavigationBar
import net.dev.weather.components.WeatherIcon
import net.dev.weather.data.model.WeatherCurrent
import net.dev.weather.data.model.WeatherHourly
import net.dev.weather.sampleData
import net.dev.weather.ui.model.PlaceWithCurrentWeather
import net.dev.weather.utils.backgroundImageFromWeather
import net.dev.weather.utils.dayOfWeek
import net.dev.weather.utils.fromAqiIndex
import kotlin.math.roundToInt

@Composable
fun CurrentWeatherScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: CurrentWeatherViewModel = hiltViewModel(),
) {

    Scaffold(
        bottomBar = bottomNavigationBar(navController = navController),
        modifier = modifier.fillMaxSize()
    )
    { paddingValues ->

        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        //TODO: tutaj obsłużyć loading
        uiState.placeWithCurrentWeather?.let { weather ->
            Content(weather, Modifier.padding(paddingValues))
        }
    }
}

@Composable
private fun Content(data: PlaceWithCurrentWeather, modifier: Modifier = Modifier) {
    val now = Clock.System.todayIn(TimeZone.currentSystemDefault())

    Column(
        modifier = modifier
            .padding(5.dp)
            .verticalScroll(rememberScrollState())
    ) {
        CardBox(data.place, data.weather.current, data.airPollutionCurrent)
        Spacer(modifier = Modifier.height(20.dp))
        HourForecast(data.weather.hourly/*.takeWhile { it.dt.date == now }*/)
        Spacer(modifier = Modifier.height(20.dp))
        CurrentWeatherDetails(data.weather.current)
    }
}

@Composable
fun CardBox(location: String, data: WeatherCurrent, airQuality: Int) {
    Card(
        modifier = Modifier
            .height(250.dp)
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopStart) {
            Image(
                painterResource(id = backgroundImageFromWeather(data.weatherCondition)),
                contentDescription = stringResource(R.string.weather_condition),
                contentScale = ContentScale.Crop,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary.copy(alpha = 0.5f), blendMode = BlendMode.SrcOver),
                modifier = Modifier
                    .fillMaxWidth()
            )

            Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 20.dp)) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(
                        text = stringResource(R.string.forecast_updated_on, data.dt.date),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                    Text(
                        text = stringResource(R.string.forecast_updated_on_time, data.dt.time.toString().substringBeforeLast(":")),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(painter = painterResource(R.drawable.outline_location_on_24), contentDescription = stringResource(R.string.place), colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary))
                        Text(text = location, style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onPrimary)
                    }
                    Text(text = stringResource(R.string.temperatureC, data.temp), style = MaterialTheme.typography.displayLarge, color = MaterialTheme.colorScheme.onPrimary)
                }
                Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = stringResource(R.string.air_quality), style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSecondary)
                    Text(text = fromAqiIndex(airQuality), style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSecondary)
                }
            }
        }
    }
}

@Composable
fun HourForecast(forecast: List<WeatherHourly>) {
    LazyRow(/*modifier = Modifier.horizontalScroll(rememberScrollState())*/) {
        items(forecast) { item ->
            HourForecastItem(item)
        }
    }
}

@Composable
fun HourForecastItem(item: WeatherHourly) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(5.dp)
            .border(1.dp, shape = RoundedCornerShape(8.dp), color = MaterialTheme.colorScheme.primaryContainer)
            .width(70.dp)
    ) {
        Text(text = dayOfWeek(item.dt.dayOfWeek), style = MaterialTheme.typography.bodySmall)
        WeatherIcon(item.weatherIcon)
        Text(text = item.dt.time.toString(), style = MaterialTheme.typography.bodySmall)
        Text(text = "${item.temp.roundToInt()}°", modifier = Modifier.padding(bottom = 5.dp))
    }

}

@Composable
fun CurrentWeatherDetails(current: WeatherCurrent) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column {
            DetailsItem(
                name = stringResource(R.string.sunrise),
                value = current.sunrise.toString().substringBeforeLast(":"),
                icon = R.drawable.outline_wb_twilight_24
            )
            DetailsItem(
                name = stringResource(R.string.pressure),
                value = "${current.pressure} hPa",
                icon = R.drawable.outline_tune_24
            )
            DetailsItem(
                name = stringResource(R.string.uv_Index),
                value = current.uvi.roundToInt().toString(),
                icon = R.drawable.eyeglasses_24px,
            )
            DetailsItem(
                name = stringResource(R.string.rain),
                value = "${current.rain.roundToInt()} mm/24h",
                icon = R.drawable.rainy_24px,
            )
        }

        Column {
            DetailsItem(
                name = stringResource(R.string.sunset),
                value = current.sunset.toString().substringBeforeLast(":"),
                icon = R.drawable.outline_nightlight_24
            )
            DetailsItem(
                name = stringResource(R.string.humidity),
                value = "${current.humidity} %",
                icon = R.drawable.humidity_percentage_24px
            )
            DetailsItem(
                name = stringResource(R.string.wind),
                value = "${current.wind.roundToInt()} km/h ${current.windDirection}",
                icon = R.drawable.outline_air_24
            )
            DetailsItem(
                name = stringResource(R.string.feels_like),
                value = "${current.feels_like.roundToInt()} °",
                icon = R.drawable.thermostat_24px,
            )
        }
    }
}

@Composable
fun DetailsItem(name: String, value: String, @DrawableRes icon: Int) {
    Row(
        modifier = Modifier
            .padding(8.dp)
            //TODO; tutaj pozbyć się stałej szerokości
            .width(180.dp)
            .background(color = MaterialTheme.colorScheme.primaryContainer, shape = RoundedCornerShape(10.dp))
    ) {
        Icon(
            painter = painterResource(icon), tint = MaterialTheme.colorScheme.primary, contentDescription = name, modifier = Modifier
                .padding(10.dp)
                .background(color = MaterialTheme.colorScheme.onPrimary, shape = RoundedCornerShape(100))
        )
        Column(modifier = Modifier.padding(10.dp)) {
            Text(text = name, style = MaterialTheme.typography.bodySmall)
            Text(text = value)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ContentPreview() {
    Content(data = sampleData)
}

@Preview(showBackground = true)
@Composable
fun BoxPreview() {
    CardBox(location = sampleData.place, data = sampleData.weather.current, airQuality = sampleData.airPollutionCurrent)
}

@Preview(showBackground = true)
@Composable
fun DetailsItemPreview() {
    DetailsItem(name = stringResource(R.string.sunrise), value = "06:00", icon = R.drawable.outline_wb_twilight_24)
}
