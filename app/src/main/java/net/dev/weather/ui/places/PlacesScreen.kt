@file:OptIn(ExperimentalMaterial3Api::class)

package net.dev.weather.ui.places

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissState
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.dev.weather.NavRoutes
import net.dev.weather.R
import net.dev.weather.bottomNavigationBar
import net.dev.weather.components.WeatherTopAppBarWithAction
import net.dev.weather.data.model.Place
import net.dev.weather.data.model.deviceCurrentLocation


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlacesScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: PlacesViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            WeatherTopAppBarWithAction(
                titleRes = R.string.places_screen_title,
                actionIcon = Icons.Default.Search, // lub: R.drawable.outline_search_24
                actionIconContentDescription = stringResource(id = R.string.search),
                onActionClick = {
                    navController.navigate(NavRoutes.Search.route)
                },
            )
        },
        bottomBar = bottomNavigationBar(
            navController = navController
        ),

        modifier = modifier.fillMaxSize()
    ) { paddingValues ->

        uiState.favorites?.let { places ->
            Content(
                places,
                uiState.currentPlaceId,
                onItemClick = {
                    scope.launch {
                        viewModel.setCurrentPlace(it)

                        withContext(Dispatchers.Main) {
                            navController.navigate(NavRoutes.CurrentWeather.route)
                        }
                    }
                },
                onItemRemoved = {
                    scope.launch {
                        viewModel.removeFromFavorites(it)
                    }
                }, modifier = Modifier.padding(paddingValues)
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun Content(places: List<Place>, currentPlaceId: String?, onItemClick: (Place) -> Unit, onItemRemoved: (Place) -> Unit, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        items(
            items = places,
            key = { place -> place.id },
            itemContent = { item ->

                val currentItem by rememberUpdatedState(item)

                val dismissState = rememberDismissState(
                    confirmValueChange = {
                        if (currentItem.id == deviceCurrentLocation.id) {
                            return@rememberDismissState false
                        }

                        if (it == DismissValue.DismissedToStart) {
                            onItemRemoved(currentItem)
                            true
                        } else {
                            false
                        }
                    }
                )

                SwipeToDismiss(
                    state = dismissState,
                    directions = setOf(DismissDirection.EndToStart),

              /*      dismissThresholds = { _ ->
                        FractionalThreshold(0.25f)
                    },*/
                    background = {
                        SwipeBackground(dismissState)
                    },
                    dismissContent = {
                        SavedPlace(item, currentPlaceId == item.id, onItemClick = onItemClick)
                    },
                    modifier = Modifier
                        .padding(1.dp)
                        .animateItemPlacement()
                )
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeBackground(dismissState: DismissState) {
    val color by animateColorAsState(
        when (dismissState.targetValue) {
            DismissValue.Default -> Color.Transparent
            DismissValue.DismissedToStart -> Color.Red
            else -> Color.Transparent
        }
    )

    val scale by animateFloatAsState(
        if (dismissState.targetValue == DismissValue.Default) 0.8f else 1.5f
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color)
            .padding(horizontal = 20.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        if (dismissState.targetValue == DismissValue.DismissedToStart)
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete",
                modifier = Modifier.scale(scale),
            )
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
        leadingContent =  {
            Image(painter = painterResource(R.drawable.outline_location_on_24), contentDescription = stringResource(R.string.place))
        },
        trailingContent = {
            if (isSelected)
                Image(
                    painter = painterResource(R.drawable.outline_check_24),
                    contentDescription = stringResource(R.string.place),
                    colorFilter = ColorFilter.tint(Color.White),
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primary, shape = CircleShape)
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
