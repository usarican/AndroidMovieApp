package com.example.mymovieapp.features.profile.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit

@Composable
fun ProfileText(
    text : String,
    textAttributes: TextAttributes,
    modifier: Modifier
    ){
    Text(
        text = text,
        modifier = modifier,
        color = textAttributes.textColor,
        maxLines = 1,
        fontFamily = textAttributes.textFontFamily,
        fontWeight = textAttributes.textFontWeight,
        fontSize = textAttributes.fontSize,
    )
}

