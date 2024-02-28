package com.example.mymovieapp.features.profile.ui.components

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

data class TextAttributes(
    val textColor : Color,
    val textFontFamily: FontFamily = playFairFontFamily,
    val textFontWeight: FontWeight,
    val fontSize: TextUnit
)

val titleTextAttributes = TextAttributes(
    textColor = h1TextColor,
    fontSize = 32.sp,
    textFontWeight = FontWeight.Bold
)

val subTitle1TextAttributes = TextAttributes(
    textColor = h2TextColor,
    fontSize = 16.sp,
    textFontWeight = FontWeight.Normal
)

val buttonTitleTextAttributes = TextAttributes(
    textColor = buttonTextColor,
    fontSize = 16.sp,
    textFontWeight = FontWeight.Medium
)