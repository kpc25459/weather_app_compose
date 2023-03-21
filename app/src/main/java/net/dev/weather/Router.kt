package net.dev.weather

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

sealed class Screen(
    val route: String,
    @StringRes val titleResourceId: Int,

    @DrawableRes val iconResourceId: Int
) {
    object CurrentWeather : Screen("current_weather", R.string.current_weather_screen_title, R.drawable.outline_home_24)
    object WeatherForecast : Screen("weather_forecast", R.string.weather_forecast_screen_title, R.drawable.baseline_format_list_bulleted_24)
    object AirQuality : Screen("air_quality", R.string.air_quality_screen_title, R.drawable.outline_air_24)
    object Search : Screen("search", R.string.search_screen_title, R.drawable.outline_map_24)
}

@Composable
fun getTitleByRoute(route: String): String {
    return when (route) {
        NavRoutes.CurrentWeather.route -> stringResource(id = Screen.CurrentWeather.titleResourceId)
        NavRoutes.WeatherForecast.route -> stringResource(id = Screen.WeatherForecast.titleResourceId)
        NavRoutes.AirQuality.route -> stringResource(id = Screen.AirQuality.titleResourceId)
        NavRoutes.Search.route -> stringResource(id = Screen.Search.titleResourceId)
        else -> stringResource(R.string.weather_forecast)
    }
}
