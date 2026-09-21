package com.unsulliedcode

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToString
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.assertTrue
import com.unsulliedcode.ui.home.HomeScreen

@RunWith(AndroidJUnit4::class)
class RouteCompletenessTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testHomeScreenIsNotEmpty() {
        composeTestRule.setContent {
            HomeScreen(onNavigate = {})
        }
        val tree = composeTestRule.onRoot().printToString()
        assertTrue("Home screen must render text or content, not be blank", tree.contains("Problems Solved") || tree.contains("Text"))
    }
}
