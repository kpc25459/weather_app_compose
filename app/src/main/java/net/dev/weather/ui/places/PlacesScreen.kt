@file:OptIn(ExperimentalMaterialApi::class)

package net.dev.weather.ui.places

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
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
import net.dev.weather.data.Place
import net.dev.weather.theme.primaryColor
import net.dev.weather.theme.tabBarBackgroundColor
import net.dev.weather.theme.tabBarTextColor

@Composable
fun PlacesScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: PlacesViewModel = hiltViewModel(),
    scaffoldState: ScaffoldState = rememberScaffoldState()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = topBar(onSearchButtonClick = {
            navController.navigate("search")
        }),
        bottomBar = bottomNavigationBar(
            navController = navController
        ),
        scaffoldState = scaffoldState,
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->

        uiState.places?.let { places ->
            Content(
                places,
                onItemClick = {
                    scope.launch {
                        viewModel.setCurrentLocation(it)

                        withContext(Dispatchers.Main) {
                            navController.navigate(NavRoutes.CurrentWeather.route)
                        }
                    }
                },
                onItemRemoved = {
                    scope.launch {
                        viewModel.removePlace(it)
                    }
                }, modifier = Modifier.padding(paddingValues)
            )
        }
    }
}

@Composable
private fun topBar(onSearchButtonClick: () -> Unit): @Composable () -> Unit {
    return {
        TopAppBar(title = { Text(text = stringResource(id = R.string.places_screen_title)) },
            backgroundColor = tabBarBackgroundColor,
            contentColor = tabBarTextColor,
            elevation = 0.dp,
            modifier = Modifier.fillMaxWidth(),
            actions = {
                SearchMenu(onSearchButtonClick)
            })
    }
}

@Composable
private fun SearchMenu(onSearchButtonClick: () -> Unit) {
    IconButton(onClick = onSearchButtonClick) {
        Icon(
            painter = painterResource(id = R.drawable.outline_search_24), contentDescription = stringResource(id = R.string.search)
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Content(places: List<Place>, onItemClick: (Place) -> Unit, onItemRemoved: (Place) -> Unit, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        items(
            items = places,
            key = { place -> place.id },
            itemContent = { item ->

                val currentItem by rememberUpdatedState(item)

                val dismissState = rememberDismissState(
                    confirmStateChange = {
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
                    dismissThresholds = { _ ->
                        FractionalThreshold(0.25f)
                    },
                    background = {
                        SwipeBackground(dismissState)
                    },
                    dismissContent = {
                        SavedPlace(item, onItemClick = onItemClick)
                    },
                    modifier = Modifier.padding(1.dp)
                        .animateItemPlacement()
                )
            }
        )
    }
}

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
        if (dismissState.targetValue == DismissValue.Default) 0.8f else 1.2f
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color)
            .padding(horizontal = 20.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "Delete",
            modifier = Modifier.scale(scale)
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SavedPlace(place: Place, onItemClick: (Place) -> Unit = {}) {
    ListItem(
        text = { Text(text = place.name) },
        secondaryText = { Text(text = place.description + ", lat: ${place.latitude}, lon: ${place.longitude}") },
        modifier = Modifier
            .padding(5.dp)
            .border(1.dp, shape = RoundedCornerShape(8.dp), color = primaryColor)
            .clickable { onItemClick(place) },
        icon = {
            Image(painter = painterResource(R.drawable.outline_location_on_24), contentDescription = stringResource(R.string.place))
        },
        trailing = {
            Image(
                painter = painterResource(R.drawable.outline_check_24),
                contentDescription = stringResource(R.string.place),
                colorFilter = ColorFilter.tint(Color.White),
                modifier = Modifier
                    .background(primaryColor, shape = CircleShape)
            )
        },

        )
}


@Composable
@Preview(showBackground = true)
fun SavedPlacePreview() {
    SavedPlace(Place("1", "London", "London, UK", 51.5074, 0.1278))
}

@Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {

}
