package net.dev.weather.ui.weatherForecast

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.dev.weather.R

@Composable
fun ExpandableListItem(
    modifier: Modifier = Modifier,
    expanded: Boolean = false,
    headlineContent: @Composable () -> Unit = {},
    leadingContent: @Composable () -> Unit = {},
    trailingContent: @Composable () -> Unit = {
        ListItemArrow(expanded = expanded, onClick = { /*onClick()*/ })
    },
    content: @Composable ColumnScope.() -> Unit = {},
    expandedContent: @Composable ColumnScope.() -> Unit = {}
) {
    ListItem(
        modifier = modifier,
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
        trailingContent = trailingContent
    )
}

@Composable
private fun ListItemArrow(expanded: Boolean, onClick: () -> Unit = {}) {
    if (expanded) {
        IconButton(onClick = { onClick() }) {
            Icon(Icons.Filled.KeyboardArrowUp, contentDescription = stringResource(R.string.arrow_up))
        }
    } else {
        IconButton(onClick = { onClick() }) {
            Icon(Icons.Filled.KeyboardArrowDown, contentDescription = stringResource(R.string.arrow_down))
        }
    }
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
@Preview
fun DayForecastItemPreview() {
    ExpandableListItem()
}
