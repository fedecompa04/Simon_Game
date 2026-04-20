package com.compagnofederico.simon.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.compagnofederico.simon.R

/**
 * Screen displaying the history of all games played.
 * @param history List of strings representing game sequences.
 * @param currentSequence Sequence of the game currently being played.
 */
@Composable
fun Screen2(history: List<String>, currentSequence: String){
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
            modifier = Modifier.padding(start = 20.dp, top = 24.dp, end = 20.dp, bottom = 12.dp)
        )
        // Scrollable list that displays the history of the played matches
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Display completed matches
            items(history) { game ->
                MatchResult(game, inProgress = false)
            }
            // Show the current game (0 elements means in progress)
            if (currentSequence.isEmpty()) {
                item {
                    MatchResult(game = currentSequence, inProgress = true)
                }
            }
        }
    }
}

/**
 * Component representing a single game result card.
 * @param game String representation of the color sequence.
 * @param inProgress Boolean flag to distinguish between a finished game and an active one.
 */
@Composable
private fun MatchResult(game: String, inProgress: Boolean){
    val cont: Int = countChar(game)
    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        // Active games are highlighted with an orange color
        color = if(inProgress) Color(0xFFFFB300) else MaterialTheme.colorScheme.surfaceVariant,
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
                text = cont.toString(),
                modifier = Modifier.width(30.dp),
                fontWeight = FontWeight.Bold
            )
            // Shows the color sequence or the "ongoing" string if in progess
            Text(
                text = if(inProgress) stringResource(R.string.ongoing) else game,
                fontStyle = if(inProgress) FontStyle.Italic else FontStyle.Normal,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

/**
 * Helper function to calculate the number of letters in the string that represents the number of
 * colored buttons clicked.
 * @param s Sequence string.
 * @return Integer count of letters in the sequence.
 */
private fun countChar(s: String): Int{
    var count = 0
    for(c in s){
        if(c != ',' && c != ' ') count++
    }
    return count
}