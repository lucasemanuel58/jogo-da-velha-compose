package com.lucasemanuel.jogodavelha.core

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class GameTest {

    @Test
    fun `vitoria em linha reta`() {
        val game = Game()
        game.play(Position(0, 0)) // X
        game.play(Position(2, 0)) // O
        game.play(Position(0, 1)) // X
        game.play(Position(2, 1)) // O
        game.play(Position(0, 2)) // X

        assertEquals(Outcome.Win(Mark.X), game.outcome)
    }

    @Test
    fun `empate quando tabuleiro fecha sem vencedor`() {
        val game = Game()
        listOf(
            Position(0, 0), Position(0, 1), Position(0, 2),
            Position(1, 0), Position(1, 2), Position(1, 1),
            Position(2, 0), Position(2, 2), Position(2, 1)
        ).forEach { game.play(it) }

        assertEquals(Outcome.Draw, game.outcome)
    }

    @Test
    fun `jogada em casa ocupada eh rejeitada`() {
        val game = Game()
        assertTrue(game.play(Position(1, 1)))
        assertFalse(game.play(Position(1, 1)))
        assertEquals(Mark.X, game.cell(1, 1))
        assertEquals(Mark.O, game.turn)
    }

    @Test
    fun `IA dificil bloqueia ameaca de vitoria`() {
        val game = Game(difficulty = Difficulty.HARD)
        game.play(Position(0, 0)) // X
        game.play(Position(1, 0)) // O
        game.play(Position(1, 1)) // X, ameaça a diagonal

        assertEquals(Position(2, 2), game.aiMove())
    }

    @Test
    fun `IA dificil aproveita vitoria imediata`() {
        val game = Game(difficulty = Difficulty.HARD)
        game.play(Position(1, 1)) // X
        game.play(Position(0, 0)) // O
        game.play(Position(2, 2)) // X
        game.play(Position(0, 1)) // O
        game.play(Position(2, 0)) // X

        assertEquals(Position(0, 2), game.aiMove())
    }

    @Test
    fun `IA facil escolhe a primeira casa livre`() {
        val game = Game(difficulty = Difficulty.EASY)
        game.play(Position(1, 1))

        assertEquals(Position(0, 0), game.aiMove())
    }

    @Test
    fun `partida encerrada nao aceita novas jogadas`() {
        val game = Game()
        game.play(Position(0, 0))
        game.play(Position(1, 0))
        game.play(Position(0, 1))
        game.play(Position(1, 1))
        game.play(Position(0, 2))

        assertFalse(game.play(Position(2, 2)))
    }

    @Test
    fun `reset limpa tabuleiro e placar interno`() {
        val game = Game()
        game.play(Position(0, 0))
        game.reset()

        assertEquals(Outcome.InProgress, game.outcome)
        assertEquals(Mark.X, game.turn)
        assertEquals(null, game.cell(0, 0))
    }
}
