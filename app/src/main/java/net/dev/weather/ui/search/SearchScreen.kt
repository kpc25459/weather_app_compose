package net.dev.weather.ui.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.dev.weather.R
import net.dev.weather.data.model.Suggestion

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onPlaceClick: (placeId: String) -> Unit = {},
    viewModel: SearchViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val coroutineScope = rememberCoroutineScope()

    if (uiState.isLoading) {
        /* Text(text = "Loading")*/
    } else {
        WeatherSearchBar(
            suggestions = uiState.suggestions ?: emptyList(),
            query = uiState.query ?: "",
            onQueryChange = { viewModel.onSearchTextChanged(it) },
            onSuggestionClick = { suggestion ->
                coroutineScope.launch {
                    viewModel.setCurrentPlace(suggestion)

                    withContext(Dispatchers.Main) {
                        onPlaceClick(suggestion.id)
                    }
                }
            },
            onFavoriteClick = { suggestion ->
                coroutineScope.launch {
                    coroutineScope.launch { viewModel.toggleFavorite(suggestion) }
                }
            },
            onBackClick = onBackClick
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherSearchBar(
    suggestions: List<Suggestion>,
    query: String,
    onQueryChange: (String) -> Unit = {},
    onFavoriteClick: (Suggestion) -> Unit = {},
    onSuggestionClick: (Suggestion) -> Unit = {},
    onBackClick: () -> Unit
) {
    var active by rememberSaveable { mutableStateOf(false) }

    Box(
        Modifier
            .fillMaxSize()
            .semantics { isTraversalGroup = true }) {
        SearchBar(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .semantics { traversalIndex = -1f },
            query = query,
            onQueryChange = { onQueryChange(it) },
            onSearch = { active = false },
            active = active,
            onActiveChange = {
                active = it
            },
            placeholder = { Text("Wyszukaj miejsca") },
            leadingIcon = {
                Icon(Icons.Default.ArrowBack, contentDescription = null, modifier = Modifier.clickable {
                    onQueryChange("")
                    active = false
                    onBackClick()
                })
            },
            trailingIcon = {
                Icon(Icons.Default.Clear, contentDescription = null, modifier = Modifier.clickable {
                    onQueryChange("")
                })
            },


            ) {
            LazyColumn(
                contentPadding = PaddingValues(start = 16.dp, top = 72.dp, end = 16.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(count = suggestions.size) { index ->
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
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .clickable { onSuggestionClick(suggestions[index]) }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {

}
