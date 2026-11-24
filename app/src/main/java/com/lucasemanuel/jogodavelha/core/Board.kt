package com.lucasemanuel.jogodavelha.core

enum class Mark {
    X, O;

    val other: Mark get() = if (this == X) O else X
}

enum class Difficulty { EASY, HARD }

data class Position(val row: Int, val col: Int) {
    init {
        require(row in 0..2 && col in 0..2) { "Posição fora do tabuleiro: $this" }
    }
}

sealed interface Outcome {
    data class Win(val winner: Mark) : Outcome
    data object Draw : Outcome
    data object InProgress : Outcome
}

typealias Board = Array<Array<Mark?>>

object BoardRules {
    private const val SIZE = 3

    private val lines: List<List<Position>> = listOf(
        listOf(Position(0, 0), Position(0, 1), Position(0, 2)),
        listOf(Position(1, 0), Position(1, 1), Position(1, 2)),
        listOf(Position(2, 0), Position(2, 1), Position(2, 2)),
        listOf(Position(0, 0), Position(1, 0), Position(2, 0)),
        listOf(Position(0, 1), Position(1, 1), Position(2, 1)),
        listOf(Position(0, 2), Position(1, 2), Position(2, 2)),
        listOf(Position(0, 0), Position(1, 1), Position(2, 2)),
        listOf(Position(0, 2), Position(1, 1), Position(2, 0))
    )

    fun newBoard(): Board = Array(SIZE) { arrayOfNulls(SIZE) }

    fun copy(board: Board): Board = Array(SIZE) { row -> Array(SIZE) { col -> board[row][col] } }

    fun winner(board: Board): Mark? {
        lines.forEach { line ->
            val first = board[line[0].row][line[0].col]
            if (first != null && line.all { board[it.row][it.col] == first }) return first
        }
        return null
    }

    fun isDraw(board: Board): Boolean = board.all { row -> row.all { it != null } }

    fun availableMoves(board: Board): List<Position> = buildList {
        for (row in 0 until SIZE) {
            for (col in 0 until SIZE) {
                if (board[row][col] == null) add(Position(row, col))
            }
        }
    }

    fun evaluate(board: Board): Outcome =
        winner(board)?.let { Outcome.Win(it) }
            ?: if (isDraw(board)) Outcome.Draw else Outcome.InProgress
}
