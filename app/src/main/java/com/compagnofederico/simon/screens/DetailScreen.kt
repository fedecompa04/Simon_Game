package com.compagnofederico.simon.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.drawBehind
import com.compagnofederico.simon.R
import com.compagnofederico.simon.components.formatText
import com.compagnofederico.simon.database.Match

/**
 * Overview layout component of game's details.
 * @param match The match we want to see the details of
 */
@Composable
fun DetailScreen(match: Match){
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    if(!isLandscape){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 32.dp)
                .verticalScroll(rememberScrollState())
        ){
            Header()
            Spacer(modifier = Modifier.height(28.dp))
            Score(match, isLandscape)
            Spacer(modifier = Modifier.height(48.dp))
            GameSequence(match)
        }
    }else{
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f) // Prende metà spazio
            ) {
                Header()
                Score(match, isLandscape)
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                GameSequence(match)
            }
        }
    }
}

/**
 * Header of the DetailScreen
 */
@Composable
fun Header(){
    Text(
        text = stringResource(R.string.detail_header),
        fontFamily = FontFamily.Monospace,
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.ExtraBold,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp, bottom = 12.dp)
    )
}

/**
 * Score of the match
 * @param match The match we want to see the details of
 * @param isLandscape Flag detecting the orientation of the device
 */
@Composable
private fun Score(match: Match, isLandscape: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = if (isLandscape) 4.dp else 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.detail_score),
            style = MaterialTheme.typography.labelLarge,
            color = Color.Gray,
            letterSpacing = 6.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(18.dp))

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(200.dp)
                // Draws a circle that contains our score
                .drawBehind {
                    drawCircle(
                        color = Color(0xFF6200EE),
                        style = Stroke(width = 8.dp.toPx())
                    )
                }
        ) {
            Text(
                text = match.score.toString(),
                style = MaterialTheme.typography.displayLarge,
                fontSize = 80.sp,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.primary,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}

/**
 * Formatted game sequence that shows where the user has committed the error.
 * @param match The match we want to see the details of
 */
@Composable
private fun GameSequence(match: Match){
    Text(
        text = stringResource(R.string.detail_sequence),
        style = MaterialTheme.typography.titleSmall
    )
    Spacer(modifier = Modifier.height(18.dp))
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = MaterialTheme.shapes.medium
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 20.dp),
            text = formatText(match.sequence, match.errorPosition),
            style = MaterialTheme.typography.titleLarge
        )
    }
}
