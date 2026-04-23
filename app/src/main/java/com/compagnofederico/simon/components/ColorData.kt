// Compagno Federico 2101752

package com.compagnofederico.simon.components

import androidx.compose.ui.graphics.Color

/**
 * Data structure representing the colors of the game.
 * Each entry maps a color to a letter.
 * @property letter Single character identifier used for sequence strings.
 * @property color Compose color value used to represent the UI buttons.
 */
enum class ColorData(val letter: String, val color: Color){
    RED("R", Color.Red),
    GREEN("G", Color.Green),
    BLUE("B", Color.Blue),
    MAGENTA("M", Color.Magenta),
    YELLOW("Y", Color.Yellow),
    CYAN("C", Color.Cyan)
}