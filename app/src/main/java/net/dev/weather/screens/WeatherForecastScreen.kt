@file:OptIn(ExperimentalLifecycleComposeApi::class)

package net.dev.weather.screens

import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.datetime.LocalDateTime
import net.dev.weather.api.WeatherServiceApi
import net.dev.weather.data.EXPANSTION_TRANSITION_DURATION
import net.dev.weather.data.ExpandableCardModel
import net.dev.weather.data.WeatherRepository
import net.dev.weather.theme.cardCollapsedBackgroundColor
import net.dev.weather.theme.cardExpandedBackgroundColor
import net.dev.weather.viewmodels.WeatherForecastViewModel
import java.time.format.TextStyle
import java.util.*
import net.dev.weather.R

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun WeatherForecastScreen() {

    val repository = WeatherRepository(WeatherServiceApi.create())

    val viewModel = WeatherForecastViewModel(repository)

    val cards by viewModel.cards.collectAsStateWithLifecycle()
    val expandedCardIds by viewModel.expandedCardIdsList.collectAsStateWithLifecycle()

    LazyColumn {
        items(cards, ExpandableCardModel::id) { card ->
            ExpandableCard(
                card = card,
                onCardArrowClick = { viewModel.onCardArrowClicked(card.id) },
                expanded = expandedCardIds.contains(card.id)
            )
            DayForecastItem(card)
            Divider()
        }
    }
}

@SuppressLint("UnusedTransitionTargetStateParameter")
@Composable
fun ExpandableCard(card: ExpandableCardModel, onCardArrowClick: () -> Unit, expanded: Boolean) {

    val transitionState = remember {
        MutableTransitionState(expanded).apply {
            targetState = !expanded
        }
    }

    val transition = updateTransition(transitionState, label = "transition")

    val cardBgColor by transition.animateColor({
        tween(durationMillis = EXPANSTION_TRANSITION_DURATION)
    }, label = "bgColorTransition") {
        if (expanded) cardExpandedBackgroundColor else cardCollapsedBackgroundColor
    }

    val cardPaddingHorizontal by transition.animateDp({
        tween(durationMillis = EXPANSTION_TRANSITION_DURATION)
    }, label = "paddingTransition") {
        if (expanded) 48.dp else 24.dp
    }

    val cardElevation by transition.animateDp({
        tween(durationMillis = EXPANSTION_TRANSITION_DURATION)
    }, label = "elevationTransition") {
        if (expanded) 24.dp else 4.dp
    }

    val cardRoundedCorners by transition.animateDp({
        tween(
            durationMillis = EXPANSTION_TRANSITION_DURATION, easing = FastOutSlowInEasing
        )
    }, label = "cornersTransition") {
        if (expanded) 0.dp else 16.dp
    }

    val arrowRotationDegree by transition.animateFloat({
        tween(durationMillis = EXPANSTION_TRANSITION_DURATION)
    }, label = "rotationDegreeTransition") {
        if (expanded) 0f else 180f
    }

    val context = LocalContext.current
    val contentColour = remember {
        Color(ContextCompat.getColor(context, R.color.colorDayNightPurple))
    }

    Card(
        backgroundColor = cardBgColor,
        contentColor = contentColour,
        elevation = cardElevation,
        shape = RoundedCornerShape(cardRoundedCorners),
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = cardPaddingHorizontal, vertical = 8.dp
            )
    ) {
        Column {
            Box {
                CardArrow(
                    degrees = arrowRotationDegree,
                    onClick = onCardArrowClick
                )
                CardTitle(title = localDate(card.weatherHourly.dt))
            }
            ExpandableContent(visible = expanded)
        }
    }
}

@Composable
fun CardArrow(degrees: Float, onClick: () -> Unit) {
    IconButton(onClick = onClick, content = {
        Icon(
            painter = painterResource(id = R.drawable.ic_expand_less_24),
            contentDescription = "Expandable Arrow",
            modifier = Modifier.rotate(degrees),
        )
    })
}

@Composable
fun CardTitle(title: String) {
    Text(
        text = title,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        textAlign = TextAlign.Center,
    )
}

@Composable
fun ExpandableContent(visible: Boolean = true, content: @Composable () -> Unit = {}) {
    val enterTransition = remember {
        expandVertically(
            expandFrom = Alignment.Top, animationSpec = tween(EXPANSTION_TRANSITION_DURATION)
        ) + fadeIn(
            initialAlpha = 0.3f, animationSpec = tween(EXPANSTION_TRANSITION_DURATION)
        )
    }

    val exitTransition = remember {
        shrinkVertically(
            // Expand from the top.
            shrinkTowards = Alignment.Top, animationSpec = tween(EXPANSTION_TRANSITION_DURATION)
        ) + fadeOut(
            // Fade in with the initial alpha of 0.3f.
            animationSpec = tween(EXPANSTION_TRANSITION_DURATION)
        )
    }

    AnimatedVisibility(visible = visible, enter = enterTransition, exit = exitTransition) {
        Column(modifier = Modifier.padding(8.dp)) {
            Spacer(modifier = Modifier.heightIn(100.dp))
            Text(
                text = "Expandable content here", textAlign = TextAlign.Center
            )
        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DayForecastItem(card: ExpandableCardModel) {

    val item = card.weatherHourly

    //TODO: tutaj przerobić nazwy miesiąca na ładne polskie
    ListItem(
        text = {
            Text(
                text = localDate(item.dt)
            )
        },
        secondaryText = {
            Text(text = item.weather[0].description)
        },
        icon = { Icon(Icons.Filled.Favorite, contentDescription = null) },
        trailing = {
            //  Text(text = "${item.temp.roundToInt()}°C")

            Icon(
                Icons.Filled.KeyboardArrowDown, contentDescription = "Localized Description"
            )

        }
    )
}

/*@Composable
private fun localDate(item: WeatherHourly) = "${item.dt.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())}, ${item.dt.dayOfMonth} ${
    item.dt.month.getDisplayName(
        TextStyle.FULL, Locale.getDefault()
    )
}"*/

private fun localDate(dt: LocalDateTime) = "${dt.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())}, ${dt.dayOfMonth} ${
    dt.month.getDisplayName(
        TextStyle.FULL, Locale.getDefault()
    )
}"
