package com.example.mymovieapp.features.profile.ui.components

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.example.mymovieapp.features.profile.data.TextAttributes

@Composable
fun ProfileText(
    text : String,
    textAttributes: TextAttributes,
    modifier: Modifier = Modifier,
    textAlign : TextAlign = TextAlign.Center
    ){
    Text(
        text = text,
        modifier = modifier,
        textAlign = textAlign,
        color = textAttributes.textColor,
        maxLines = 1,
        fontFamily = textAttributes.textFontFamily,
        fontWeight = textAttributes.textFontWeight,
        fontSize = textAttributes.fontSize,
    )
}

