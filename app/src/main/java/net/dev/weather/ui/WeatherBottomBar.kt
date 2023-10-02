package net.dev.weather.ui

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import net.dev.weather.navigation.TopLevelDestination

@Composable
fun weatherBottomBar(
    destinations: List<TopLevelDestination>,
    onNavigateTopLevelDestination: (TopLevelDestination) -> Unit,
    currentDestination: NavDestination?,
    modifier: Modifier = Modifier
): @Composable () -> Unit {
    return {
        NavigationBar {

            destinations.forEach { destination ->

                val selected = currentDestination?.isTopLevelDestination(destination)

                //TODO: custom navigationbaritem
                NavigationBarItem(
                    icon = {
                        Icon(imageVector = destination.selectedIcon, contentDescription = null)
                    },
                    selected = selected ?: false,
                    onClick = { onNavigateTopLevelDestination(destination) },
                    modifier = modifier
                )
            }
        }
    }
}

private fun NavDestination?.isTopLevelDestination(destination: TopLevelDestination): Boolean =
    this?.hierarchy?.any { it.route?.contains(destination.name, ignoreCase = true) ?: false } ?: false