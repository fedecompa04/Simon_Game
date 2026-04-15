package com.compagnofederico.simon.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.compagnofederico.simon.R

@Composable
fun Screen2(history: List<String>, currentSequence: String){
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ){
        items(history) {
            game -> MatchResult(game, inProgress = false)
        }
        if(currentSequence.isEmpty()) {
            item {
                MatchResult(game = currentSequence, inProgress = true)
            }
        }
    }
}

@Composable
private fun MatchResult(game: String, inProgress: Boolean){
    val cont: Int = countChar(game)
    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        color = if(inProgress) Color(0xFFFFB300) else MaterialTheme.colorScheme.surfaceVariant,
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            Text(
                text = cont.toString(),
                modifier = Modifier.width(30.dp),
                fontWeight = FontWeight.Bold
            )

            Text(
                text = if(inProgress) stringResource(R.string.ongoing) else game,
                fontStyle = if(inProgress) FontStyle.Italic else FontStyle.Normal,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

private fun countChar(s: String): Int{
    var count = 0
    for(c in s){
        if(c != ',' && c != ' ') count++
    }
    return count
}