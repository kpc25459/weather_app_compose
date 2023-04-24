@file:OptIn(ExperimentalComposeUiApi::class)

package net.dev.weather.ui.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import net.dev.weather.NavRoutes
import net.dev.weather.R
import net.dev.weather.bottomNavigationBar
import net.dev.weather.theme.tabBarBackgroundColor
import net.dev.weather.theme.tabBarTextColor

@Composable
fun SearchScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = hiltViewModel(),
    scaffoldState: ScaffoldState = rememberScaffoldState()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        //topBar = topBar(),
        topBar = {
            SearchBar(
                searchText = uiState.query ?: "",
                onSearchTextChanged = { viewModel.onSearchTextChanged(it) },
                onNavigateBack = {
                    navController.navigate(NavRoutes.CurrentWeather.route)
                },
            )
        },
        bottomBar = bottomNavigationBar(navController = navController), scaffoldState = scaffoldState, modifier = modifier.fillMaxSize()
    ) { paddingValues ->

        uiState.matchedCities?.let { matchedCities ->
            Content(matchedCities, Modifier.padding(paddingValues))
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchBar(
    searchText: String,
    placeholderText: String = stringResource(id = R.string.search),
    onSearchTextChanged: (String) -> Unit = {},
    onClearClick: () -> Unit = {},
    onNavigateBack: () -> Unit = {}
) {

    var showClearButton by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }

    TopAppBar(
        title = { Text(text = "") },
        navigationIcon = {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack, contentDescription = "back"
                )
            }
        }, actions = {

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp)
                    .onFocusChanged { focusState ->
                        showClearButton = (focusState.isFocused)
                    }
                    .focusRequester(focusRequester),
                value = searchText,
                onValueChange = onSearchTextChanged,
                placeholder = {
                    Text(text = placeholderText)
                },
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    backgroundColor = Color.Transparent,
                    cursorColor = LocalContentColor.current.copy(alpha = LocalContentAlpha.current)
                ),
                trailingIcon = {
                    AnimatedVisibility(
                        visible = showClearButton,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        IconButton(onClick = { onClearClick() }) {
                            Icon(
                                imageVector = Icons.Filled.Close, contentDescription = "clear content"
                            )
                        }
                    }
                },
                maxLines = 1,
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    keyboardController?.hide()
                })
            )
        })

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

@Composable
private fun topBar(): @Composable () -> Unit {
    return {
        TopAppBar(title = { Text(text = stringResource(id = R.string.search_screen_title)) },
            backgroundColor = tabBarBackgroundColor,
            contentColor = tabBarTextColor,
            elevation = 0.dp,
            modifier = Modifier.fillMaxWidth(),
            actions = {
                SearchMenu()
            })
    }
}

@Composable
private fun SearchMenu() {
    IconButton(onClick = { /*TODO*/ }) {
        Icon(
            painter = painterResource(id = R.drawable.outline_search_24), contentDescription = stringResource(id = R.string.search)
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun Content(matchedCities: List<String>, modifier: Modifier = Modifier) {
    LazyColumn {
        items(matchedCities.size) { index ->
            ListItem(
                text = { Text(text = matchedCities[index]) },
                trailing = {
                    Image(painter = painterResource(R.drawable.round_add_24), contentDescription = stringResource(R.string.place))
                }
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {

}
