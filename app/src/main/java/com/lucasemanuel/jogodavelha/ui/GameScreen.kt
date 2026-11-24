package com.lucasemanuel.jogodavelha.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lucasemanuel.jogodavelha.core.Difficulty
import com.lucasemanuel.jogodavelha.core.Mark
import com.lucasemanuel.jogodavelha.core.Outcome

@Composable
fun GameScreen(viewModel: GameViewModel = viewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Jogo da Velha", style = MaterialTheme.typography.headlineMedium)

            Text(
                text = "X: ${state.score.x}   O: ${state.score.o}   Empates: ${state.score.draws}",
                style = MaterialTheme.typography.titleMedium
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { viewModel.toggleMode() }) {
                    Text(if (state.vsCpu) "Contra PC" else "2 jogadores")
                }
                Button(onClick = { viewModel.reset() }) {
                    Text("Reiniciar")
                }
            }

            if (state.vsCpu) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = state.difficulty == Difficulty.EASY,
                        onClick = { viewModel.setDifficulty(Difficulty.EASY) },
                        label = { Text("Fácil") }
                    )
                    FilterChip(
                        selected = state.difficulty == Difficulty.HARD,
                        onClick = { viewModel.setDifficulty(Difficulty.HARD) },
                        label = { Text("Difícil") }
                    )
                }
            }

            Text(resultText(state), style = MaterialTheme.typography.titleLarge)

            BoardGrid(state, viewModel::onCellClick)
        }
    }
}

private fun resultText(state: GameUiState): String = when (val outcome = state.outcome) {
    is Outcome.Win -> "${outcome.winner.name} venceu!"
    Outcome.Draw -> "Empate!"
    Outcome.InProgress -> "Vez de ${state.turn.name}"
}

@Composable
private fun BoardGrid(state: GameUiState, onCellClick: (Int, Int) -> Unit) {
    val humanTurn = !state.vsCpu || state.turn == Mark.X

    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        for (row in 0..2) {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                for (col in 0..2) {
                    val mark = state.board[row][col]
                    Button(
                        onClick = { onCellClick(row, col) },
                        modifier = Modifier
                            .size(84.dp)
                            .semantics { contentDescription = "casa-$row-$col" },
                        enabled = mark == null &&
                            state.outcome is Outcome.InProgress &&
                            humanTurn
                    ) {
                        Text(
                            text = mark?.name ?: "",
                            style = MaterialTheme.typography.displayMedium,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}
