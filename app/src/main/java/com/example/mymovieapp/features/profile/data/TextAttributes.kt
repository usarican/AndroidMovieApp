package com.example.mymovieapp.features.profile.data

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.example.mymovieapp.features.profile.ui.components.buttonTextColor
import com.example.mymovieapp.features.profile.ui.components.h1TextColor
import com.example.mymovieapp.features.profile.ui.components.h2TextColor
import com.example.mymovieapp.features.profile.ui.components.playFairFontFamily

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