// Compagno Federico 2101752

package com.compagnofederico.simon.components

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle

/**
 * Formats the game sequence string, coloring the wrong inputs in red.
 *
 * @param s The comma-separated color sequence string.
 * @param errorPosition The index where the user committed the error.
 * @return AnnotatedString with red text applied to the error part.
 */
fun formatText(s: String, errorPosition: Int): AnnotatedString{
    val colors: List<String> = s.split(", ")
    val correctPart = colors.take(errorPosition).joinToString(", ")
    val wrongPart = colors.drop(errorPosition).joinToString(", ")
    val formattedText = buildAnnotatedString {
        append(correctPart)
        if(correctPart.isNotEmpty() && wrongPart.isNotEmpty()){
            append(", ")
        }
        withStyle(style = SpanStyle(color = Color.Red)){
            append(wrongPart)
        }
    }
    return formattedText
}