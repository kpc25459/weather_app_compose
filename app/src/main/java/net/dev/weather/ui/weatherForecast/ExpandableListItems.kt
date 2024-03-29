package net.dev.weather.ui.weatherForecast

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.dev.weather.R

@Composable
fun <T> ExpandableListItems(
    modifier: Modifier = Modifier,
    items: List<T>,
    headlineContent: @Composable (T) -> Unit = {},
    leadingContent: @Composable (T) -> Unit = {},
    content: @Composable ColumnScope.(T) -> Unit = {},
    expandedContent: @Composable ColumnScope.(T) -> Unit = {},
    bottomContent: @Composable () -> Unit = {}
) {
    var expandedCardsIdxs by rememberSaveable { mutableStateOf<List<String>>(mutableListOf()) }

    items.forEachIndexed { index, item ->
        ExpandableListItem(
            modifier = modifier,
            expanded = expandedCardsIdxs.contains(index.toString()),
            headlineContent = { headlineContent(item) },
            leadingContent = { leadingContent(item) },
            content = { content(item) },
            expandedContent = { expandedContent(item) },
            onClick = {
                expandedCardsIdxs = if (expandedCardsIdxs.contains(index.toString())) {
                    expandedCardsIdxs.filter { it != index.toString() }
                } else {
                    expandedCardsIdxs + index.toString()
                }
            }
        )

        bottomContent()
    }

}

@Composable
private fun ExpandableListItem(
    modifier: Modifier = Modifier,
    expanded: Boolean = false,
    headlineContent: @Composable () -> Unit = {},
    leadingContent: @Composable () -> Unit = {},
    content: @Composable ColumnScope.() -> Unit = {},
    expandedContent: @Composable ColumnScope.() -> Unit = {},
    onClick: () -> Unit = {},
) {
    val interactionSource = remember { MutableInteractionSource() }

    ListItem(
        modifier = modifier.then(Modifier.clickable(indication = null, interactionSource = interactionSource) { onClick() }),
        headlineContent = headlineContent,
        supportingContent = {
            Column {
                content()
                AnimatedVisibility(visible = expanded) {
                    Column {
                        Spacer(modifier = Modifier.height(5.dp))
                        expandedContent()
                    }
                }
            }
        },
        leadingContent = leadingContent,
        trailingContent = {
            ListItemArrow(expanded = expanded, onClick = onClick)
        }
    )
}

@Composable
fun ListItemRow(label: String, value: String) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(text = label)
        Text(text = value, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 10.dp))
    }
}

@Composable
private fun ListItemArrow(expanded: Boolean, onClick: () -> Unit = {}) {
    if (expanded) {
        IconButton(onClick = onClick) {
            Icon(Icons.Filled.KeyboardArrowUp, contentDescription = stringResource(R.string.arrow_up))
        }
    } else {
        IconButton(onClick = onClick) {
            Icon(Icons.Filled.KeyboardArrowDown, contentDescription = stringResource(R.string.arrow_down))
        }
    }
}

@Composable
@Preview
fun ExpandableListItemsPreview() {
    ExpandableListItems(
        items = listOf("1", "2", "3"),
    ) {
        ListItemRow(label = "label", value = "value")
    }
}

@Composable
@Preview
fun ListItemRowPreview() {
    ListItemRow(label = "label", value = "value")
}

@Composable
@Preview
fun ListItemArrowPreview() {
    ListItemArrow(expanded = false)
}

@Composable
@Preview
fun ExpandableListItemPreview() {
    ExpandableListItem(
        headlineContent = { Text(text = "headline") },
        leadingContent = { Text(text = "leading") },
        content = {
            Text(text = "content")
        },
        expandedContent = {
            Text(text = "expandedContent")
        }
    )
}
