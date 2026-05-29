// Compagno Federico 2101752

package com.compagnofederico.simon.screens

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import com.compagnofederico.simon.components.ColorData
import com.compagnofederico.simon.R
import com.compagnofederico.simon.components.GameViewModel

/**
 * Primary game screen where the user interacts with the Simon sequence.
 * @param onEndGame Callback function called when the game is finished, passing the recorded sequence.
 */
@Composable
fun GameScreen(onEndGame: (List<String>) -> Unit, viewModel: GameViewModel) {
    // --- METTI IN PAUSA AUTOMATICAMENTE SE L'APP VA IN BACKGROUND ---
    LifecycleEventEffect(Lifecycle.Event.ON_STOP) {
        // Se il computer stava giocando, forziamo la pausa
        if (viewModel.isComputerPlaying && !viewModel.isPaused) {
            viewModel.togglePause()
        }
    }

    // --- RIPRENDI AUTOMATICAMENTE QUANDO L'UTENTE RIAPRE L'APP (Opzionale) ---
    // Se preferisci che l'utente debba premere "Play" manualmente per riprendere,
    // cancella pure questo blocco ON_START qui sotto.
    LifecycleEventEffect(Lifecycle.Event.ON_START) {
        if (viewModel.isGameStarted && viewModel.isPaused) {
            viewModel.togglePause()
        }
    }
    // rememberSaveable: keeps the sequence state alive even during screen rotations
    val sequence = rememberSaveable{mutableStateListOf<String>()}
    // Get current device configuration
    val orientation = LocalConfiguration.current
    val isGameOver = !viewModel.isGameStarted && sequence.isNotEmpty()
    BackHandler(enabled = viewModel.isGameStarted && !isGameOver) {
        viewModel.onEndGame()
        onEndGame(sequence)
        sequence.clear()
    }
    LaunchedEffect(viewModel.isComputerPlaying) {
        // Svuota la sequenza locale SOLO nell'istante in cui il computer prende il controllo e inizia a giocare
        if (viewModel.isComputerPlaying) {
            sequence.clear()
        }
    }
    // PORTRAIT MODE
    if(orientation.orientation == Configuration.ORIENTATION_PORTRAIT){
        Column(
            modifier = Modifier
              .fillMaxSize()
              // safeDrawing handles the spaces for system bars and camera
              .windowInsetsPadding(WindowInsets.safeDrawing)
              .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            ColorGrid(
                onColorClick = { color->
                    sequence.add(color)
                    viewModel.onUserClick(color) },
                viewModel.isUserClickable,
                viewModel.highlightedColor,
                viewModel.isGameStarted,
                isGameOver = isGameOver
            )
            Display(
                if(viewModel.isComputerPlaying) emptyList() else sequence,
                viewModel.isGameStarted,
                isGameOver
            )
            ButtonArea(
                onStart = {
                    viewModel.startMatch()
                },
                onPause = {
                    viewModel.togglePause()
                },
                onEndGame = {
                    viewModel.onEndGame()
                    onEndGame(sequence)
                    sequence.clear()
                },
                viewModel.isComputerPlaying,
                viewModel.isGameStarted,
                viewModel.isPaused,
                isGameOver
            )
        }
    // LANDSCAPE MODE
    }else{
        Row(
            modifier = Modifier
                .fillMaxSize()
                // safeDrawing handles the spaces for system bars and camera
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(32.dp)
        ){
            Box(modifier=Modifier
                .weight(0.5f)
                .fillMaxHeight()
            ) {
                ColorGrid(
                    onColorClick = { color->
                        sequence.add(color)
                        viewModel.onUserClick(color) },
                    viewModel.isUserClickable,
                    viewModel.highlightedColor,
                    viewModel.isGameStarted,
                    isGameOver
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Display(
                    if(viewModel.isComputerPlaying) emptyList() else sequence,
                    viewModel.isGameStarted,
                    isGameOver
                )
                ButtonArea(
                    onStart = {
                        viewModel.startMatch()
                    },
                    onPause = {
                        viewModel.togglePause()
                    },
                    onEndGame = {
                        viewModel.onEndGame()
                        onEndGame(sequence)
                        sequence.clear()
                    },
                    viewModel.isComputerPlaying,
                    viewModel.isGameStarted,
                    viewModel.isPaused,
                    isGameOver
                )
            }
        }
    }
}

/**
 * Single button used in the game grid.
 * @param colorItem Data object containing the color value and the associated letter.
 * @param onClick Callback function invoked when a colored button is clicked, passing the color's
 * initial letter.
 */
@Composable
private fun ColorButton(colorItem: ColorData, onClick: (String) -> Unit, isUserClickable: Boolean,
                        highlightedColor: String?, isGameStarted: Boolean){
    val isHighlighted = colorItem.letter == highlightedColor
    val baseColor = when{
        isHighlighted -> colorItem.color
        else -> colorItem.color.copy(alpha = 0.4f)
    }
    Box(
        modifier = Modifier
            .padding(4.dp)
            .aspectRatio(4f/3f)
            .background(baseColor)
            .clickable(enabled = isGameStarted && isUserClickable) { // Disabilitiamo il click durante il turno del PC
                onClick(colorItem.letter)
            }
            .border(
                width = if (isHighlighted) 6.dp else 0.dp,
                color = if (isHighlighted) Color.White else Color.Transparent,
            )
    )
}

/**
 * A 3x2 grid of "ColorButton" displaying the six colors of the game.
 * @param onColorClick Callback function invoked when any color button in the grid is clicked.
 */
@Composable
private fun ColorGrid(onColorClick: (String) -> Unit, isUserClickable: Boolean,
                      highlightedColor: String?, isGameStarted: Boolean, isGameOver: Boolean){
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        userScrollEnabled = false
    ) {
        items(ColorData.entries) { colorItem ->
            ColorButton(
                colorItem = colorItem,
                onClick = onColorClick,
                isUserClickable = isUserClickable && !isGameOver,
                highlightedColor = highlightedColor,
                isGameStarted = isGameStarted && !isGameOver
            )
        }
    }
}

/**
 * A display area that shows the current sequence of the initials of the clicked colors.
 * @param sequence List of color initials selected by the user.
 */
@Composable
private fun Display(sequence: List<String>, isGameStarted: Boolean, isGameOver: Boolean){
    val placeholderText = stringResource(R.string.placeholder)
    val starterText = stringResource(R.string.starter)
    val textShown = when {
        isGameOver -> stringResource(R.string.game_over)
        sequence.isEmpty() -> if (isGameStarted) placeholderText else starterText
        else -> sequence.joinToString(", ")
    }


    // Auto-scroll logic: whenever the text updates, scroll to the bottom of the box
    val scrollState = rememberScrollState()
    LaunchedEffect(textShown){
        scrollState.animateScrollTo(scrollState.maxValue)
    }
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(75.dp),
        color = Color(0xFFEEEEEE),
        shape = MaterialTheme.shapes.medium
    ){
        Box(
            modifier = Modifier
                .padding(12.dp)
                .verticalScroll(scrollState)
        ){
            Text(
                text = textShown,
                color = when {
                    isGameOver -> Color.Red
                    sequence.isEmpty() && isGameStarted -> Color.Gray
                    else -> Color.Black
                },
                fontWeight = if (isGameOver) FontWeight.ExtraBold else FontWeight.Normal
            )
        }
    }
}

/**
 * Area containing "Clear" and "EndGame" buttons
 * @param onClear Callback function invoked when the user wants to reset the current sequence
 * @param onEndGame Callback function invoked when the user wants to finish the game and save the result
 */
@Composable
private fun ButtonArea(onStart: () -> Unit, onPause: () -> Unit, onEndGame: () -> Unit,
                       isComputerPlaying: Boolean, isGameStarted: Boolean, isPaused: Boolean, isGameOver: Boolean){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ){
        Button(
            onClick = onStart,
            enabled = !isGameStarted && !isGameOver,
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(4.dp) // Riduce il padding interno per far stare il testo
        ){
            Text(
                text = stringResource(R.string.button_start),
                maxLines = 1,
                textAlign = TextAlign.Center
            )
        }

        Button(
            onClick = onPause,
            enabled = isComputerPlaying && !isGameOver,
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(4.dp)
        ){
            Text(
                text = if(!isPaused) {
                    stringResource(R.string.button_pause)
                }else{
                    stringResource(R.string.button_resume)
                },
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        }

        Button(
            onClick = onEndGame,
            enabled = isGameStarted && !isGameOver,
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(4.dp)
        ){
            Text(
                text = stringResource(R.string.button_end),
                maxLines = 1,
                textAlign = TextAlign.Center
            )
        }
    }
}