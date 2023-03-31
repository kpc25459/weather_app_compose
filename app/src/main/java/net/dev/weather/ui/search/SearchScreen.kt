package net.dev.weather.ui.search

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import net.dev.weather.ui.currentWeather.CurrentWeatherViewModel
import net.dev.weather.R
import net.dev.weather.api.WeatherServiceApi
import net.dev.weather.bottomNavigationBar
import net.dev.weather.data.Main
import net.dev.weather.data.NetworkRepository
import net.dev.weather.theme.tabBarBackgroundColor
import net.dev.weather.theme.tabBarTextColor
import net.dev.weather.ui.airQuality.*

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun SearchScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = SearchViewModel(NetworkRepository(WeatherServiceApi.create())),
    scaffoldState: ScaffoldState = rememberScaffoldState()
) {

    Scaffold(
        topBar = topBar(),
        bottomBar = bottomNavigationBar(navController = navController),
        scaffoldState = scaffoldState,
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->

/*
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        uiState.main?.let { main ->
            Content(main, modifier = Modifier.padding(paddingValues))
        }*/
    }
}

@Composable
private fun topBar(): @Composable () -> Unit {
    return {
        TopAppBar(
            title = { Text(text = stringResource(id = R.string.search_screen_title)) },
            backgroundColor = tabBarBackgroundColor,
            contentColor = tabBarTextColor,
            elevation = 0.dp,
            modifier = Modifier.fillMaxWidth(),
            actions = {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        painter = painterResource(id = R.drawable.outline_search_24),
                        contentDescription = stringResource(id = R.string.search)
                    )
                }
            }
        )
    }
}

@Composable
private fun Content(data: Main, modifier: Modifier = Modifier) {

}


@Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {

}
