@file:OptIn(ExperimentalMaterialApi::class)

package net.dev.weather.ui.places

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
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
            Content(places, Modifier.padding(paddingValues))
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

@Composable
private fun Content(places: List<Place>, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        items(places.size) { index ->
            SavedPlace(places[index])
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SavedPlace(place: Place) {
    ListItem(
        text = { Text(text = place.name) },
        secondaryText = { Text(text = place.description) },
        modifier = Modifier
            .padding(5.dp)
            .border(1.dp, shape = RoundedCornerShape(8.dp), color = primaryColor),
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
        }
    )
}


@Composable
@Preview(showBackground = true)
fun SavedPlacePreview() {
    SavedPlace(Place("1", "London", "London, UK"))
}

@Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {

}
