package net.dev.weather.components

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.NavigationRail
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun WeatherNavigationRail(
    modifier: Modifier = Modifier,
    header: @Composable (ColumnScope.() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {

    NavigationRail(
        modifier = modifier,
        header = header,
        content = content
    )
}