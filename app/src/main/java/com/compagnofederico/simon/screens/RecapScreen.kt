// Compagno Federico 2101752

package com.compagnofederico.simon.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.compagnofederico.simon.R
import com.compagnofederico.simon.components.GameViewModel
import com.compagnofederico.simon.components.formatText
import com.compagnofederico.simon.database.Match

/**
 * Screen displaying the history of all games played.
 * @param history List of strings representing game sequences.
 */
@Composable
fun RecapScreen(viewModel: GameViewModel, onGameScreen: () -> Unit, onDetailScreen: (Match) -> Unit){
    // Carichiamo i dati all'avvio dello schermo
    LaunchedEffect(Unit) {
        viewModel.loadMatches()
    }

    // Osserviamo la variabile matchHistory del ViewModel
    val history by viewModel.matchHistory
    Box(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                // safeDrawing handles the spaces for system bars and camera
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {
            // Screen Title
            Text(
                text = stringResource(R.string.summary),
                fontFamily = FontFamily.Monospace,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, top = 24.dp, end = 20.dp, bottom = 12.dp)
            )

            when {
                viewModel.isLoadingMatches -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                        ){
                            CircularProgressIndicator()
                    }
                }
                history.isEmpty() -> {
                    EmptyHistoryState(modifier = Modifier.weight(1f))
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        items(history) { match ->
                            MatchResult(
                                match.score,
                                match.sequence,
                                match.errorPosition,
                                onClick = { onDetailScreen(match) }
                            )
                        }
                    }
                }
            }
        }
        FloatingActionButton(
            onClick = onGameScreen,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp),
        ) {
            Icon(Icons.Default.PlayArrow, stringResource(R.string.content_desc_FAB))
        }
    }
}


/**
 * Component representing a single game result card.
 * @param game String representation of the color sequence.
 */
@Composable
private fun MatchResult(score: Int, game: String, errorPosition: Int, onClick: () -> Unit){
    Surface(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        // Active games are highlighted with an orange color
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            // Shows the score on the left
            Text(
                text = score.toString(),
                modifier = Modifier.width(30.dp),
                fontWeight = FontWeight.Bold
            )
            // Shows the color sequence
            Text(
                text = formatText(game, errorPosition),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun EmptyHistoryState(modifier: Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.empty_list1),
            fontFamily = FontFamily.Monospace,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
        Text(
            text = stringResource(R.string.empty_list2),
            fontFamily = FontFamily.Monospace,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray.copy(alpha = 0.7f),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}