package com.lucasemanuel.jogodavelha.ui

import com.lucasemanuel.jogodavelha.core.Board
import com.lucasemanuel.jogodavelha.core.BoardRules
import com.lucasemanuel.jogodavelha.core.Difficulty
import com.lucasemanuel.jogodavelha.core.Mark
import com.lucasemanuel.jogodavelha.core.Outcome

data class Score(val x: Int = 0, val o: Int = 0, val draws: Int = 0) {
    fun after(outcome: Outcome): Score = when (outcome) {
        is Outcome.Win -> if (outcome.winner == Mark.X) copy(x = x + 1) else copy(o = o + 1)
        Outcome.Draw -> copy(draws = draws + 1)
        Outcome.InProgress -> this
    }
}

data class GameUiState(
    val board: Board = BoardRules.newBoard(),
    val turn: Mark = Mark.X,
    val outcome: Outcome = Outcome.InProgress,
    val score: Score = Score(),
    val vsCpu: Boolean = true,
    val difficulty: Difficulty = Difficulty.HARD
)
