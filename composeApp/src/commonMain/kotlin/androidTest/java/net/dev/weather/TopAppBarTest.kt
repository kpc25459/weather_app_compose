package net.dev.weather

import android.content.Context
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.test.platform.app.InstrumentationRegistry
import net.dev.weather.components.WeatherTopAppBar
import net.dev.weather.navigation.TopLevelDestination
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalMaterial3Api::class)
class TopAppBarTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun weatherTopAppBar_airQualityScreenIsDisplayed() {

        composeTestRule.setContent {
            WeatherTopAppBar(currentDestination = TopLevelDestination.AIR_QUALITY)
        }

        val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
        val titleText = context.resources.getString(TopLevelDestination.AIR_QUALITY.titleTextId)

        composeTestRule
            .onNodeWithContentDescription(titleText)
            .assertIsDisplayed()
    }

    @Test
    fun weatherTopAppBar_onCurrentWeatherNoTopAppBar() {

        composeTestRule.setContent {
            WeatherTopAppBar(currentDestination = TopLevelDestination.CURRENT_WEATHER)
        }

        val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
        val titleText = context.resources.getString(TopLevelDestination.CURRENT_WEATHER.titleTextId)

        composeTestRule
            .onNodeWithContentDescription(titleText)
            .assertIsNotDisplayed()
    }
}