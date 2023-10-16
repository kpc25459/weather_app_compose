package net.dev.weather.navigation

import androidx.annotation.DrawableRes
import net.dev.weather.R

enum class TopLevelDestination(
    @DrawableRes val icon: Int,
    val titleTextId: Int
) {

    CURRENT_WEATHER(
        icon = R.drawable.outline_home_24,
        titleTextId = R.string.current_weather_screen_title
    ),

    WEATHER_FORECAST(
        icon = R.drawable.baseline_format_list_bulleted_24,
        titleTextId = R.string.weather_forecast_screen_title
    ),

    AIR_QUALITY(
        icon = R.drawable.outline_air_24,
        titleTextId = R.string.air_quality_screen_title
    ),

    PLACES(
        icon = R.drawable.outline_map_24,
        titleTextId = R.string.places_screen_title
    )
}