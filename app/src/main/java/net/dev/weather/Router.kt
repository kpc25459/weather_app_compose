package net.dev.weather

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

sealed class Screen(
    val route: String,
    @StringRes val titleResourceId: Int,

    @DrawableRes val iconResourceId: Int
) {
    object CurrentWeather : Screen("current_weather", R.string.current_weather_screen_title, R.drawable.outline_home_24)
    object WeatherForecast : Screen("weather_forecast", R.string.weather_forecast_screen_title, R.drawable.baseline_format_list_bulleted_24)
    object AirQuality : Screen("air_quality", R.string.air_quality_screen_title, R.drawable.outline_air_24)

    object Places : Screen("places", R.string.places_screen_title, R.drawable.outline_map_24)

    object Search : Screen("search", R.string.search_screen_title, R.drawable.outline_map_24)
}