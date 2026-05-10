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
    private val playerSequence = mutableStateListOf<String>()
    var highlightedColor by mutableStateOf<String?>(null)
    var isComputerPlaying by mutableStateOf(false)
    var isGameOver by mutableStateOf(false)
    var isGameStarted by mutableStateOf(false)
    var isPaused by mutableStateOf(false)
    private val simonColors = listOf("R", "G", "B", "M", "Y", "C")

    fun togglePause(){
        if(isComputerPlaying){
            isPaused = !isPaused
        }
    }
    fun startMatch(){
        isGameOver = false
        isGameStarted = true
        computerSequence.clear()
        playerSequence.clear()
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
            delay(1000)
            for(color in computerSequence){
                while(isPaused){
                    delay(1000)
                }
                highlightedColor = color
                // soundHelper.play(color)
                delay(800)
                highlightedColor = null
                delay(200)
            }
            isComputerPlaying = false
        }
    }

    fun onUserClick(color: String){
        if(!isGameStarted || isComputerPlaying || isGameOver) return
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
            isGameStarted = false
            computerSequence.clear()
            playerSequence.clear()
        }
    }
}