package com.compagnofederico.simon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.*
import com.compagnofederico.simon.screens.Screen1
import com.compagnofederico.simon.screens.Screen2
import com.compagnofederico.simon.ui.theme.SimonTheme

/**
 * Main entry point of the application.
 * Manages navigation and the state for game history.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent{
            SimonTheme {
                val navController = rememberNavController()
                val gameHistory = rememberSaveable{ mutableStateListOf<String>() }
                val sequenceString = rememberSaveable{mutableStateOf("")}
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = "Screen1",
                        // Deal with refresh time in the changing between a screen and the other one
                        enterTransition = { fadeIn(animationSpec = tween(150)) },
                        exitTransition = { fadeOut(animationSpec = tween(150)) }
                    ){
                        // Main game screen
                        composable("Screen1") {
                            Screen1(
                                onEndGame = { sequenceList ->
                                    sequenceString.value = sequenceList.joinToString(", ")
                                    if(sequenceString.value.isNotEmpty()) gameHistory.add(sequenceString.value)
                                    navController.navigate("Screen2")
                                }
                            )
                        }
                        // History summary screen
                        composable("Screen2") {
                            Screen2(gameHistory, sequenceString.value)
                        }
                    }

                }
            }
        }
    }
}