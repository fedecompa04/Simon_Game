package com.compagnofederico.simon.components

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle

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