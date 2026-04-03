package com.compagnofederico.simon.screens

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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.compagnofederico.simon.components.ColorData

@Composable
fun Screen1() {
    // Stato persistente alla rotazione
    val sequence = rememberSaveable { mutableStateListOf<String>() }

    // Layout portrait: colonna verticale
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ){
        ColorGrid(
            Modifier.weight(2f),
            onColorClick = {sequence.add(it)}
        )
    }
}

@Composable
fun ColorGrid(modifier: Modifier = Modifier, onColorClick: (String) -> Unit){
    LazyVerticalGrid(
        // Fisso il numero di colonne
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        // La rendo non scrollabile
        userScrollEnabled = false
    ) {
        items(ColorData.entries) { colorItem ->
            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .aspectRatio(0.75f)
                    .background(colorItem.color)
                    .clickable {
                        onColorClick(colorItem.letter)
                    }
            )
        }
    }
}