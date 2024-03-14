package com.zj.weather.view.city.viewmodel

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.dp
import com.zj.weather.MainActivity
import com.zj.weather.NavGraph
import com.zj.weather.theme.PlayWeatherTheme
import com.zj.weather.view.test.ButtonTest
import org.junit.Rule
import org.junit.Test

class UiTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun calculate_20_percent_tip() {
        composeTestRule.setContent {
            PlayWeatherTheme {
                ButtonTest()
            }
        }
        composeTestRule.onNodeWithTag("zhujiang")
        composeTestRule.onNodeWithTag("testClick").performClick()
        composeTestRule.onNodeWithTag("zhujiang").assertTextEquals("liupeixing")
    }

    @Test
    fun testAnimationWithClock() {
        // Pause animations
        composeTestRule.mainClock.autoAdvance = false
        var enabled by mutableStateOf(false)
        composeTestRule.setContent {
            val color: Color by animateColorAsState(
                targetValue = if (enabled) Color.Red else Color.Green,
                animationSpec = tween(durationMillis = 2500), label = ""
            )
            Box(Modifier.size(64.dp).background(color))
        }

        // Initiate the animation.
        enabled = true

        // Let the animation proceed.
        composeTestRule.mainClock.advanceTimeBy(500L)

        // Compare the result with the image showing the expected result.
        // `assertAgainGolden` needs to be implemented in your code.
        composeTestRule.onRoot().captureToImage()
    }

    @get:Rule
    val androidComposeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testMain() {
        composeTestRule.mainClock
        composeTestRule.setContent {
            NavGraph()
        }
    }
}