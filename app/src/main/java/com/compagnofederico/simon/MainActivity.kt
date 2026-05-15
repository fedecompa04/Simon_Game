// Compagno Federico 2101752

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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import com.compagnofederico.simon.components.GameViewModel
import com.compagnofederico.simon.screens.GameScreen
import com.compagnofederico.simon.screens.RecapScreen
import com.compagnofederico.simon.ui.theme.SimonTheme

/**
 * Main entry point of the application.
 * Manages navigation and the state for game history.
 */
class MainActivity : ComponentActivity() {
    /**
     * Initializes the activity and sets up the UI architecture.
     * @param savedInstanceState Bundle containing the activity's previous state if there was one.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent{
            SimonTheme {
                val navController = rememberNavController()
                val gameHistory = rememberSaveable{ mutableStateListOf<String>() }
                val sequenceString = rememberSaveable{mutableStateOf("")}
                val gameViewModel: GameViewModel = viewModel()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = "RecapScreen",
                        // Deal with refresh time in the changing between a screen and the other one
                        enterTransition = { fadeIn(animationSpec = tween(175)) },
                        exitTransition = { fadeOut(animationSpec = tween(175)) }
                    ){
                        // Main game screen
                        composable("RecapScreen") {
                            RecapScreen(
                                viewModel = gameViewModel,
                                onGameScreen = {
                                    navController.navigate("GameScreen")
                                }
                            )
                        }
                        // History summary screen
                        composable("GameScreen") {
                            GameScreen(
                                viewModel = gameViewModel,
                                onEndGame = {
                                    gameViewModel.onEndGame()
                                }
                            )
                        }
                    }

                }
            }
        }
    }
}