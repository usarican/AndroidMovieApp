package com.example.mymovieapp.features.profile.ui.components

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun ProfileText(text : String){
    Text(
        text = text,
        color = h1TextColor,
        maxLines = 1,
        fontFamily = playFairFontFamily,
        fontWeight = FontWeight.Black,
        fontSize = 30.sp
    )
}