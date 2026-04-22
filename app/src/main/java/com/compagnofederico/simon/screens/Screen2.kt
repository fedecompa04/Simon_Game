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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.compagnofederico.simon.R

/**
 * Screen displaying the history of all games played.
 * @param history List of strings representing game sequences.
 */
@Composable
fun Screen2(history: List<String>){
    // Auto-scroll logic: whenever the list of sequences update its size the screen will auto-scroll
    // so we follow the last match played
    val scrollState = rememberLazyListState()
    LaunchedEffect(history.size){
        scrollState.animateScrollToItem(history.size - 1)
    }
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
            modifier = Modifier
                .fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            state = scrollState
        ) {
            // Display completed matches
                items(history) { game ->
                    MatchResult(game)
                }
        }
    }
}


/**
 * Component representing a single game result card.
 * @param game String representation of the color sequence.
 */
@Composable
private fun MatchResult(game: String){
    val cont: Int = countChar(game)
    Surface(
        modifier = Modifier
            .fillMaxWidth(),
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
                text = cont.toString(),
                modifier = Modifier.width(30.dp),
                fontWeight = FontWeight.Bold
            )
            // Shows the color sequence
            Text(
                text = game,
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