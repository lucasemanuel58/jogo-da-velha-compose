package com.lucasemanuel.jogodavelha.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucasemanuel.jogodavelha.core.Difficulty
import com.lucasemanuel.jogodavelha.core.Game
import com.lucasemanuel.jogodavelha.core.Mark
import com.lucasemanuel.jogodavelha.core.Outcome
import com.lucasemanuel.jogodavelha.core.Position
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GameViewModel : ViewModel() {
    private val _state = MutableStateFlow(GameUiState())
    val state = _state.asStateFlow()

    private var game = Game(difficulty = _state.value.difficulty)
    private var vsCpu = true
    private var scored = false
    private var aiJob: Job? = null

    fun onCellClick(row: Int, col: Int) {
        if (vsCpu && game.turn == Mark.O) return
        if (!game.play(Position(row, col))) return

        maybePlayAi()
        publish()
    }

    private fun maybePlayAi() {
        if (!vsCpu || game.outcome !is Outcome.InProgress || game.turn != Mark.O) return

        aiJob?.cancel()
        aiJob = viewModelScope.launch {
            delay(250)
            game.aiMove()?.let { game.play(it) }
            publish()
        }
    }

    fun setDifficulty(value: Difficulty) {
        game.setDifficulty(value)
        _state.value = state.value.copy(difficulty = value)
    }

    fun toggleMode() {
        vsCpu = !vsCpu
        reset()
    }

    fun reset() {
        aiJob?.cancel()
        game = Game(difficulty = state.value.difficulty)
        scored = false
        publish()
    }

    private fun publish() {
        var score = state.value.score
        if (game.outcome !is Outcome.InProgress && !scored) {
            score = score.after(game.outcome)
            scored = true
        }

        _state.value = GameUiState(
            board = game.snapshot(),
            turn = game.turn,
            outcome = game.outcome,
            score = score,
            vsCpu = vsCpu,
            difficulty = state.value.difficulty
        )
    }
}
