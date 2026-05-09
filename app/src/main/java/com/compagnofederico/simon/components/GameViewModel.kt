package com.compagnofederico.simon.components

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GameViewModel: ViewModel() {
    private val computerSequence = mutableStateListOf<String>()
    var highlightedColor by mutableStateOf<String?>(null)
    var isComputerPlaying by mutableStateOf(false)
    private val simonColors = listOf("R", "G", "B", "M", "Y", "C")

    fun startMatch(){
        computerSequence.clear()
        computerSequence.add(simonColors.random())
        playMatch()
    }

    private fun playMatch(){
        viewModelScope.launch{
            isComputerPlaying = true
            for(color in computerSequence){
                highlightedColor = color
                delay(500)
                highlightedColor = null
                delay(200)
            }
            isComputerPlaying = false
        }
    }
}