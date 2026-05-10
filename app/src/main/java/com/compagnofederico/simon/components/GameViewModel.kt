package com.compagnofederico.simon.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GameViewModel: ViewModel() {
    private val computerSequence = mutableStateListOf<String>()
    private val playerSequence = mutableStateListOf<String>()
    var highlightedColor by mutableStateOf<String?>(null)
    var isComputerPlaying by mutableStateOf(false)
    var isGameOver by mutableStateOf(false)
    var isGameStarted by mutableStateOf(false)
    private val simonColors = listOf("R", "G", "B", "M", "Y", "C")

    fun startMatch(){
        computerSequence.clear()
        playerSequence.clear()
        isGameOver = false
        computerSequence.add(simonColors.random())
        playMatch()
    }

    private fun addNewColor(){
        computerSequence.add(simonColors.random())
        playerSequence.clear()
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

    fun onUserClick(color: String){
        if(isComputerPlaying || isGameOver) return
        playerSequence.add(color)
        if(playerSequence[playerSequence.size - 1] == computerSequence[playerSequence.size - 1]){
            if(playerSequence.size == computerSequence.size){
                viewModelScope.launch{
                    delay(500)
                    addNewColor()
                }
            }
        }else{
            isGameOver = true
            computerSequence.clear()
            playerSequence.clear()
        }
    }
}