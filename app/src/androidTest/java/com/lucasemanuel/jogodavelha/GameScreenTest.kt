package com.lucasemanuel.jogodavelha

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.lucasemanuel.jogodavelha.ui.GameScreen
import com.lucasemanuel.jogodavelha.ui.theme.JogoDaVelhaTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GameScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun tabuleiroTemNoveCasasClicaveis() {
        composeTestRule.setContent {
            JogoDaVelhaTheme { GameScreen() }
        }

        composeTestRule.onNodeWithContentDescription("casa-0-0").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("casa-2-2").assertIsDisplayed()
    }

    @Test
    fun jogadorColocaXaoTocarNumaCasa() {
        composeTestRule.setContent {
            JogoDaVelhaTheme { GameScreen() }
        }

        composeTestRule.onNodeWithContentDescription("casa-1-1").performClick()
        composeTestRule.onNodeWithText("X").assertIsDisplayed()
    }
}
