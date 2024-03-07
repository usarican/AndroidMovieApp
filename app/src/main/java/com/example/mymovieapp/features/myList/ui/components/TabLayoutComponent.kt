package com.example.mymovieapp.features.myList.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.contentColorFor
import com.example.mymovieapp.features.profile.ui.components.ProfileText
import com.example.mymovieapp.features.profile.ui.components.playFairFontFamily
import com.example.mymovieapp.features.profile.ui.components.tabButtonSelectedBackgroundColor
import com.example.mymovieapp.features.profile.ui.components.tabButtonSelectedTextBackgroundColor
import com.example.mymovieapp.features.profile.ui.components.tabButtonUnselectedBackgroundColor
import com.example.mymovieapp.features.profile.ui.components.tabButtonUnselectedTextBackgroundColor

@Composable
fun MyListTabLayout(
    onClick: () -> Unit,
    isSelectedItem: Boolean,
    itemText: String,
    modifier: Modifier
) {
    val bgColor =
        if (isSelectedItem) tabButtonSelectedBackgroundColor else tabButtonUnselectedBackgroundColor
    val textColor =
        if(isSelectedItem) tabButtonSelectedTextBackgroundColor else tabButtonUnselectedTextBackgroundColor

    Surface(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .wrapContentHeight()
                .clickable { onClick() }
                .clip(RoundedCornerShape(8.dp))
                .background(bgColor)
                .padding(4.dp)
        ) {
            Text(
                text = itemText,
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
                fontFamily = playFairFontFamily,
                fontWeight = FontWeight.Bold,
                color = textColor,
                modifier = Modifier.fillMaxWidth().padding(4.dp)
            )

        }
    }

}