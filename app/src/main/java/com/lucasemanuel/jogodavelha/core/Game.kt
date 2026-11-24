package com.lucasemanuel.jogodavelha.core

class Game(private var difficulty: Difficulty = Difficulty.HARD) {
    private var board: Board = BoardRules.newBoard()

    var turn: Mark = Mark.X
        private set

    var outcome: Outcome = Outcome.InProgress
        private set

    fun cell(row: Int, col: Int): Mark? = board[row][col]

    fun snapshot(): Board = BoardRules.copy(board)

    fun play(position: Position): Boolean {
        if (outcome !is Outcome.InProgress) return false
        if (board[position.row][position.col] != null) return false

        board[position.row][position.col] = turn
        outcome = BoardRules.evaluate(board)
        if (outcome is Outcome.InProgress) turn = turn.other
        return true
    }

    fun aiMove(): Position? = Ai.chooseMove(board, turn, difficulty)

    fun setDifficulty(value: Difficulty) {
        difficulty = value
    }

    fun reset() {
        board = BoardRules.newBoard()
        turn = Mark.X
        outcome = Outcome.InProgress
    }
}
