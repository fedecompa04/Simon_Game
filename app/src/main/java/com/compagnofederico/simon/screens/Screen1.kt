package com.compagnofederico.simon.screens

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.unit.dp
import com.compagnofederico.simon.components.ColorData
import com.compagnofederico.simon.R

/**
 * Primary game screen where the user interacts with the Simon sequence.
 * @param onEndGame Callback function called when the game is finished, passing the recorded sequence.
 */
@Composable
fun Screen1(onEndGame: (List<String>) -> Unit) {
    // remeberSaveable: keeps the sequence state alive even during screen rotations
    val sequence = rememberSaveable { mutableStateListOf<String>() }
    // Get current device confugation
    val orientation = LocalConfiguration.current

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
                onColorClick = { sequence.add(it) },
            )
            Display(
                sequence = sequence
            )
            ButtonArea(
                onClear = { sequence.clear() },
                onEndGame = {
                    onEndGame(sequence)
                    sequence.clear()
                }
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
                    onColorClick = { sequence.add(it) },
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Display(
                    sequence = sequence
                )
                ButtonArea(
                    onClear = { sequence.clear() },
                    onEndGame = {
                        onEndGame(sequence)
                        sequence.clear()
                    }
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
private fun ColorButton(colorItem: ColorData, onClick: (String) -> Unit){
    Box(
        modifier = Modifier
            .padding(4.dp)
            .aspectRatio(4f/3f)
            .background(colorItem.color)
            .clickable {
                onClick(colorItem.letter)
            }
    )
}

/**
 * A 3x2 grid of "ColorButton" displaying the six colors of the game.
 * @param onColorClick Callback function invoked when any color button in the grid is clicked.
 */
@Composable
private fun ColorGrid(onColorClick: (String) -> Unit){
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        userScrollEnabled = false
    ) {
        items(ColorData.entries) { colorItem ->
            ColorButton(
                colorItem = colorItem,
                onClick = onColorClick,
            )
        }
    }
}

/**
 * A display area that shows the current sequence of the initials of the clicked colors.
 * @param sequence List of color initials selected by the user.
 */
@Composable
private fun Display(sequence: List<String>){
    val placeholderText = stringResource(R.string.placeholder)
    val textShown = if (sequence.isEmpty()) {
        placeholderText
    }else{
        sequence.joinToString(", ")
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
                color = if(sequence.isEmpty()) Color.Gray else Color.Black
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
private fun ButtonArea(onClear: () -> Unit, onEndGame: () -> Unit){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ){
        Button(onClick = onClear){
            Text(stringResource(R.string.button_clear))
        }
        Button(onClick = onEndGame){
            Text(stringResource(R.string.button_end))
        }
    }
}