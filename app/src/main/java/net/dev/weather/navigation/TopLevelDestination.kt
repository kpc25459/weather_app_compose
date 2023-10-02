package net.dev.weather.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Search
import androidx.compose.ui.graphics.vector.ImageVector
import net.dev.weather.R

enum class TopLevelDestination(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val iconTextId: Int,
    val titleTextId: Int
) {

    CURRENT_WEATHER(
        selectedIcon = Icons.Rounded.Home,
        unselectedIcon = Icons.Outlined.Home,
        iconTextId = R.string.current_weather_screen_title,
        titleTextId = R.string.current_weather_screen_title
    ),

    WEATHER_FORECAST(
        selectedIcon = Icons.Rounded.ArrowForward,
        unselectedIcon = Icons.Outlined.ArrowForward,
        iconTextId = R.string.weather_forecast_screen_title,
        titleTextId = R.string.weather_forecast_screen_title
    ),

    AIR_QUALITY(
        selectedIcon = Icons.Rounded.Done,
        unselectedIcon = Icons.Outlined.Done,
        iconTextId = R.string.air_quality_screen_title,
        titleTextId = R.string.air_quality_screen_title
    ),

    PLACES(
        selectedIcon = Icons.Rounded.LocationOn,
        unselectedIcon = Icons.Outlined.LocationOn,
        iconTextId = R.string.places_screen_title,
        titleTextId = R.string.places_screen_title
    ),

    SEARCH(
        selectedIcon = Icons.Rounded.Search,
        unselectedIcon = Icons.Outlined.Search,
        iconTextId = R.string.search_screen_title,
        titleTextId = R.string.search_screen_title
    )
}