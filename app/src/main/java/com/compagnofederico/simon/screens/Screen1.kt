package com.compagnofederico.simon.screens

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.compagnofederico.simon.components.ColorData
import com.compagnofederico.simon.R

@Composable
fun Screen1(onEndGame: (List<String>) -> Unit) {
    // Stato persistente alla rotazione
    val sequence = rememberSaveable { mutableStateListOf<String>() }
    val orientation = LocalConfiguration.current
    // Layout portrait: colonna verticale
    if(orientation.orientation == Configuration.ORIENTATION_PORTRAIT){
        Column(
            modifier = Modifier
              .fillMaxSize()
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
    }else{
        Row(
            modifier = Modifier
                .fillMaxSize()
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

@Composable
private fun ColorGrid(onColorClick: (String) -> Unit){
    // Elenchi Lazy: https://developer.android.com/develop/ui/compose/lists?hl=it
    LazyVerticalGrid(
        // Fisso il numero di colonne
        columns = GridCells.Fixed(2),
        // La rendo non scrollabile
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

@Composable
private fun Display(sequence: List<String>){
    val placeholderText = stringResource(R.string.placeholder)
    val textShown = if (sequence.isEmpty()) {
        placeholderText
    }else{
        sequence.joinToString(", ")
    }
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