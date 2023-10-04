package net.dev.weather.ui.places

import android.Manifest
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import net.dev.weather.MainActivityUiState
import net.dev.weather.R
import net.dev.weather.components.SwipeDismissItem
import net.dev.weather.data.model.Place
import net.dev.weather.data.model.PlaceMode
import net.dev.weather.data.model.deviceCurrentLocationPlace
import kotlin.math.min

val permissions = listOf(
    Manifest.permission.ACCESS_COARSE_LOCATION,
    Manifest.permission.ACCESS_FINE_LOCATION,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlacesScreen(
    viewModel: PlacesViewModel = hiltViewModel(),
    onPlaceClick: (placeId: String) -> Unit
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    uiState.favorites?.let { places ->
        Content(
            places,
            uiState.currentPlaceId,
            onItemClick = {
                onPlaceClick(it.id)
            },
            onItemRemoved = {
                scope.launch {
                    viewModel.removeFromFavorites(it)
                }
            }
        )
    }

    /*
        Scaffold(
            topBar = {
                WeatherTopAppBarWithAction(
                    titleRes = R.string.places_screen_title,
                    actionIcon = Icons.Default.Search, // lub: R.drawable.outline_search_24
                    actionIconContentDescription = stringResource(id = R.string.search),
                    onActionClick = {
                        navController.navigate(Search.route)
                    },
                )
            },
            bottomBar = WeatherBottomBar(
                navController = navController
            ),

            modifier = modifier.fillMaxSize()
        ) { paddingValues ->


        }*/
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Content(places: List<Place>, currentPlaceId: String?, onItemClick: (Place) -> Unit, onItemRemoved: (Place) -> Unit, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {

        item {
            SavedPlace(deviceCurrentLocationPlace, currentPlaceId == deviceCurrentLocationPlace.id, onItemClick = onItemClick)
        }

        items(
            items = places,
            key = { place -> place.id },
            itemContent = { item ->
                SwipeDismissItem(
                    background = { progress ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize(),
                            contentAlignment = Alignment.CenterEnd
                        ) {

                            if (progress != 1f) {
                                val iconAlpha: Float by animateFloatAsState(targetValue = progress, label = "icon_alpha_animation")
                                val iconScale by animateFloatAsState(targetValue = min(progress * 2, 1f), label = "icon_scale_animation")
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = stringResource(R.string.delete),
                                    tint = MaterialTheme.colorScheme.error,
                                    modifier = Modifier
                                        .size(48.dp)
                                        .align(Alignment.CenterEnd)
                                        .scale(iconScale)
                                        .graphicsLayer(alpha = iconAlpha)
                                )
                            }
                        }
                    }
                ) {
                    SavedPlace(item, currentPlaceId == item.id, onItemClick = onItemClick)
                }

            }
        )
    }
}

private fun shouldAskForPermissions(uiState: MainActivityUiState): Boolean {
    return when (uiState) {
        MainActivityUiState.Loading -> true
        is MainActivityUiState.Success -> uiState.userData.currentMode == PlaceMode.DEVICE_LOCATION
    }
}

@Composable
fun SavedPlace(place: Place, isSelected: Boolean, onItemClick: (Place) -> Unit = {}) {
    ListItem(
        headlineContent = { Text(text = place.name) },
        supportingContent = { Text(text = place.description) },
        modifier = Modifier
            .padding(5.dp)
            .border(1.dp, shape = RoundedCornerShape(8.dp), color = MaterialTheme.colorScheme.primary)
            .clickable { onItemClick(place) },
        leadingContent = {
            Image(
                painter = painterResource(R.drawable.outline_location_on_24),
                contentDescription = stringResource(R.string.place),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
            )
        },
        trailingContent = {
            if (isSelected)
                Image(
                    painter = painterResource(R.drawable.outline_check_24),
                    contentDescription = stringResource(R.string.place),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                )
        }
    )
}


@Composable
@Preview(showBackground = true)
fun SavedPlacePreview() {
    SavedPlace(Place("1", "London", "London, UK", 51.5074, 0.1278), true)
}

@Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {

}
