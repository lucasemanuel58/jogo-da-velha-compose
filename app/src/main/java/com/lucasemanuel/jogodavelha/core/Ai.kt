package com.lucasemanuel.jogodavelha.core

object Ai {
    fun chooseMove(board: Board, ai: Mark, difficulty: Difficulty): Position? {
        val moves = BoardRules.availableMoves(board)
        return when (difficulty) {
            Difficulty.EASY -> moves.firstOrNull()
            Difficulty.HARD -> moves.maxByOrNull { move ->
                val next = BoardRules.copy(board)
                next[move.row][move.col] = ai
                minimax(next, ai, ai.other, depth = 0)
            }
        }
    }

    private fun minimax(board: Board, ai: Mark, turn: Mark, depth: Int): Int {
        when (val outcome = BoardRules.evaluate(board)) {
            is Outcome.Win -> return if (outcome.winner == ai) 10 - depth else depth - 10
            Outcome.Draw -> return 0
            Outcome.InProgress -> Unit
        }

        val scores = BoardRules.availableMoves(board).map { move ->
            val next = BoardRules.copy(board)
            next[move.row][move.col] = turn
            minimax(next, ai, turn.other, depth + 1)
        }

        return if (turn == ai) scores.maxOrNull() ?: 0 else scores.minOrNull() ?: 0
    }
}
