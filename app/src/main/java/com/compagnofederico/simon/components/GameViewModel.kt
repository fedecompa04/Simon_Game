package com.compagnofederico.simon.components

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.compagnofederico.simon.database.Match
import com.compagnofederico.simon.database.MatchDao
import com.compagnofederico.simon.database.MatchDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.collections.emptyList

class GameViewModel(application: Application): AndroidViewModel(application) {
    private val computerSequence = mutableStateListOf<String>()
    private val playerSequence = mutableStateListOf<String>()
    var highlightedColor by mutableStateOf<String?>(null)
    var isComputerPlaying by mutableStateOf(false)
    var isGameStarted by mutableStateOf(false)
    var isPaused by mutableStateOf(false)
    var isUserClickable by mutableStateOf(false)
    var selectedMatch by mutableStateOf<Match?>(null)
    private var playJob: Job? = null
    private val simonColors = listOf("R", "G", "B", "M", "Y", "C")
    private val soundHelper: SoundHelper = SoundHelper(application)
    private val db = MatchDatabase.getDatabase(application)
    private val matchDao = db.matchDao()
    val matchHistory = mutableStateOf<List<Match>>(emptyList())

    fun selectMatch(match: Match) {
        selectedMatch = match
    }
    fun togglePause(){
        if(isComputerPlaying){
            isPaused = !isPaused
        }
    }
    fun startMatch(){
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
        playJob?.cancel()
        playJob = viewModelScope.launch{
            isUserClickable = false
            isComputerPlaying = true
            delay(1000)
            for(color in computerSequence){
                while(isPaused){
                    delay(1000)
                }
                if (!isActive) break
                highlightedColor = color
                soundHelper.playSound(color)
                delay(800)
                highlightedColor = null
                delay(200)
            }
            isComputerPlaying = false
            isUserClickable = true
        }
    }

    private fun gameEnded(){
        playJob?.cancel()
        isGameStarted = false
        isPaused = false
        isComputerPlaying = false
        isUserClickable = false
        computerSequence.clear()
        playerSequence.clear()
        highlightedColor = null
    }

    fun onEndGame(){
        if(isGameStarted){
            if(computerSequence.size > 1){
                saveMatch(playerSequence.size)
            }
            gameEnded()
        }
    }

    fun onUserClick(color: String){
        if(!isGameStarted || isComputerPlaying || !isUserClickable) return
        isUserClickable = false
        playJob?.cancel()
        playJob = viewModelScope.launch {
            playerSequence.add(color)
            highlightedColor = color
            soundHelper.playSound(color)
            delay(300)
            highlightedColor = null
            if (playerSequence[playerSequence.size - 1] == computerSequence[playerSequence.size - 1]) {
                if (playerSequence.size == computerSequence.size) {
                    delay(500)
                    if (isActive && isGameStarted) {
                        addNewColor()
                    }
                }else{
                    isUserClickable = true
                }
            } else {
                saveMatch(playerSequence.size - 1)
                gameEnded()
            }
        }
    }
    fun saveMatch(errorIndex: Int){
        val score = computerSequence.size - 1
        val sequence = computerSequence.joinToString(", ")
        val errorIndex = if(errorIndex < 0) 0 else errorIndex
        viewModelScope.launch(Dispatchers.IO){
            val match = Match(score = score, sequence = sequence, errorPosition = errorIndex)
            matchDao.insertMatch(match)
        }
    }
    fun loadMatches(){
        viewModelScope.launch(Dispatchers.IO){
            val matches = matchDao.getAllMatches()
            withContext(Dispatchers.Main){
                matchHistory.value = matches
            }
        }
    }
}