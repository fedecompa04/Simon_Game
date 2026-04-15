package com.compagnofederico.simon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
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
                    modifier = Modifier
                        .fillMaxSize()
                        .safeDrawingPadding(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = "Screen1",
                    ){
                        composable("Screen1") {
                            Screen1(
                                onEndGame = { sequenceList ->
                                    sequenceString.value = sequenceList.joinToString(", ")
                                    if(sequenceString.value.isNotEmpty()) gameHistory.add(sequenceString.value)
                                    navController.navigate("Screen2")
                                }
                            )
                        }
                        composable("Screen2") {
                            Screen2(gameHistory, sequenceString.value)
                        }
                    }

                }
            }
        }
    }
}
