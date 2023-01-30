package net.dev.weather.ui.currentWeather

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import net.dev.weather.R
import net.dev.weather.api.WeatherServiceApi
import net.dev.weather.components.ErrorScreen
import net.dev.weather.components.LoadingScreen
import net.dev.weather.components.WeatherIcon
import net.dev.weather.data.NetworkRepository
import net.dev.weather.data.WeatherHourly
import net.dev.weather.dayOfWeek
import net.dev.weather.fromAqiIndex
import kotlin.math.roundToInt

@Composable
fun CurrentWeatherScreen(modifier: Modifier = Modifier, viewModel: CurrentWeatherViewModel = CurrentWeatherViewModel(NetworkRepository(WeatherServiceApi.create()))) {

    val lifecycle = LocalLifecycleOwner.current.lifecycle

    val uiState by produceState<CurrentWeatherUiState>(
        initialValue = CurrentWeatherUiState.Loading,
        key1 = lifecycle,
        key2 = viewModel
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            viewModel.uiState.collect { value = it }
        }
    }

    if (uiState is CurrentWeatherUiState.Loading) {
        LoadingScreen()
    }

    if (uiState is CurrentWeatherUiState.Success) {
        CurrentWeatherScreen(
            data = (uiState as CurrentWeatherUiState.Success).data,
            modifier = modifier
        )
    }

    if (uiState is CurrentWeatherUiState.Error) {
        ErrorScreen()
    }
}

@Composable
internal fun CurrentWeatherScreen(data: MainWeather, modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .padding(5.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Box(data)
        Spacer(modifier = Modifier.height(20.dp))
        HourForecast(data)
        Spacer(modifier = Modifier.height(20.dp))
        CurrentWeatherDetails(data)
    }

}

@Preview(showBackground = true)
@Composable
fun CurrentWeatherPagePreview() {
    CurrentWeatherScreen()
}

@Composable
fun Box(data: MainWeather) {
    val currentWeather = data.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        elevation = 8.dp,
    ) {
        Image(
            painterResource(id = currentWeather.backgroundImage),
            contentDescription = stringResource(R.string.weather_condition),
            contentScale = ContentScale.Fit,
            colorFilter = ColorFilter.tint(Color.Black.copy(alpha = 0.5f), blendMode = BlendMode.SrcOver),
            modifier = Modifier
                .fillMaxWidth()
        )

        Column(modifier = Modifier.padding(10.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = stringResource(R.string.forecast_updated_on, currentWeather.dt.date), color = Color.White)
                Text(text = stringResource(R.string.forecast_updated_on_time, currentWeather.dt.time.toString().substringBeforeLast(":")), color = Color.White)
            }
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(imageVector = Icons.Filled.Place, contentDescription = stringResource(R.string.place), colorFilter = ColorFilter.tint(Color.White))
                    Text(text = data.location, color = Color.White)
                }
                Text(text = stringResource(R.string.temperatureC, currentWeather.temp), color = Color.White)
            }
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = stringResource(R.string.air_quality), color = Color.White)
                Text(text = fromAqiIndex(data.airQuality), color = Color.White)
            }
        }
    }
}

@Composable
fun HourForecast(data: MainWeather) {
    val forecast = data.hourlyForecast

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
            .border(1.dp, shape = RoundedCornerShape(8.dp), color = Color(0xFFE0EAFF))
            .width(70.dp)
    ) {
        Text(text = dayOfWeek(item.dt.dayOfWeek))
        WeatherIcon(item.weatherIcon)
        Text(text = item.dt.time.toString())
        Text(text = "${item.temp.roundToInt()}°", modifier = Modifier.padding(bottom = 5.dp))
    }

}

@Composable
fun CurrentWeatherDetails(data: MainWeather) {

    val weather = data.current

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column {
            DetailsItem(
                image = Icons.Filled.ArrowForward,
                name = stringResource(R.string.sunrise),
                value = weather.sunrise
            )
            DetailsItem(
                image = Icons.Filled.ArrowForward,
                name = stringResource(R.string.pressure),
                value = weather.pressure
            )
            DetailsItem(
                image = Icons.Filled.ArrowForward,
                name = stringResource(R.string.uv_Index),
                value = weather.uvi,
            )
            DetailsItem(
                image = Icons.Filled.ArrowForward,
                name = stringResource(R.string.rain),
                value = weather.rain,
            )
        }

        Column {
            DetailsItem(
                image = Icons.Filled.ArrowForward,
                name = stringResource(R.string.sunset),
                value = weather.sunset
            )
            DetailsItem(
                image = Icons.Filled.ArrowForward,
                name = stringResource(R.string.humidity),
                value = weather.humidity
            )
            DetailsItem(
                image = Icons.Filled.ArrowForward,
                name = stringResource(R.string.wind),
                value = weather.wind
            )
            DetailsItem(
                image = Icons.Filled.ArrowForward,
                name = stringResource(R.string.feels_like),
                value = weather.feels_like,
            )
        }
    }
}

@Composable
fun DetailsItem(image: ImageVector, name: String, value: String) {
    Row(
        modifier = Modifier
            .padding(8.dp)
            //TODO; tutaj pozbyć się stałej szerokości
            .width(180.dp)
            .background(color = Color(0xFFE0EAFF), shape = RoundedCornerShape(10.dp))
    ) {
        Icon(imageVector = image, contentDescription = null, modifier = Modifier.padding(10.dp))
        Column(modifier = Modifier.padding(10.dp)) {
            Text(text = name)
            Text(text = value)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailsItemPreview() {
    DetailsItem(image = Icons.Filled.ArrowForward, name = stringResource(R.string.sunrise), value = "06:00")
}

@Preview(showBackground = true)
@Composable
fun CurrentWeatherDetailsPreview() {
    /*Column {
        CurrentWeatherDetails(viewModel)
    }*/
}
