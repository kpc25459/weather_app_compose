package net.dev.weather.ui.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.dev.weather.CurrentWeather
import net.dev.weather.Places
import net.dev.weather.R
import net.dev.weather.bottomNavigationBar
import net.dev.weather.data.model.Suggestion

@Composable
fun SearchScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            SearchBar(
                searchText = uiState.query ?: "",
                onSearchTextChanged = { viewModel.onSearchTextChanged(it) },
                onClearClick = { viewModel.onClearClick() },
                onNavigateBack = {
                    navController.navigate(Places.route)
                },
            )
        },
        bottomBar = bottomNavigationBar(navController = navController), modifier = modifier.fillMaxSize()
    ) { paddingValues ->

        uiState.suggestions?.let { suggestions ->
            Content(
                suggestions = suggestions,
                onItemClick = { suggestion ->
                    coroutineScope.launch {
                        viewModel.setCurrentPlace(suggestion)

                        withContext(Dispatchers.Main) {
                            navController.navigate(CurrentWeather.route)
                        }
                    }
                },
                onFavoriteClick = { suggestion -> coroutineScope.launch { viewModel.toggleFavorite(suggestion) } },
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
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
        },
        actions = {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(vertical = 2.dp, horizontal = 10.dp)
                    .onFocusChanged { focusState ->
                        showClearButton = (focusState.isFocused)
                    }
                    .focusRequester(focusRequester),
                value = searchText,
                onValueChange = onSearchTextChanged,
                placeholder = {
                    Text(text = placeholderText)
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
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
        },
        modifier = Modifier.background(Color.Transparent)
    )

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

@Composable
private fun Content(suggestions: List<Suggestion>, onItemClick: (Suggestion) -> Unit, onFavoriteClick: (Suggestion) -> Unit, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        items(suggestions.size) { index ->
            ListItem(
                headlineContent = { Text(text = suggestions[index].name) },
                supportingContent = { suggestions[index].description?.let { Text(text = it) } },
                trailingContent = {
                    IconButton(onClick = { onFavoriteClick(suggestions[index]) }) {
                        Image(
                            painter = painterResource(if (suggestions[index].isFavorite) R.drawable.outline_check_24 else R.drawable.round_add_24),
                            contentDescription = stringResource(R.string.place),
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                        )
                    }
                },
                modifier = Modifier.clickable { onItemClick(suggestions[index]) }
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {

}
