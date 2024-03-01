package com.example.mymovieapp.features.profile.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mymovieapp.features.profile.data.titleTextAttributes

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun ProfileBottomSheet(
    closeCallBack : () -> Unit
){
    val sheetState = rememberModalBottomSheetState()
    var selectedIndex by remember {
        mutableStateOf(0)
    }
    ModalBottomSheet(
        onDismissRequest = { closeCallBack() },
        sheetState = sheetState
    ) {
        ProfileText(text = "Bottom Sheet", textAttributes = titleTextAttributes)
        PickerAnimationLazyColumn2(items = listOf("A","B","C","A","B","C","A","B","C","A","B","C","A","B","C",), selectedItemIndex = selectedIndex , onItemSelected = {
            selectedIndex = it
        } )
    }
}

@Composable
fun PickerAnimationLazyColumn(
    items: List<String>,
    selectedItemIndex: Int,
    onItemSelected: (Int) -> Unit
) {
    LazyColumn {
        itemsIndexed(items) { index, item ->
            val isSelected = index == selectedItemIndex
            PickerItem(
                text = item,
                isSelected = isSelected,
                onClick = { onItemSelected(index) }
            )
        }
    }
}

@Composable
fun PickerItem(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val scale = if (isSelected) 3f else 1.0f
    Text(
        text = text,
        modifier = Modifier
            .clickable {  }
            .clickable { onClick() }
            .padding(vertical = 8.dp)
            .graphicsLayer(scaleX = scale, scaleY = scale)
    )
}

@Composable
fun PickerAnimationLazyColumn2(
    items: List<String>,
    selectedItemIndex: Int,
    onItemSelected: (Int) -> Unit
) {
    val listState = rememberLazyListState()
    LazyColumn(state = listState) {
        itemsIndexed(items) { index, item ->
            val isSelected = index == selectedItemIndex
            PickerItem(
                text = item,
                isSelected = isSelected,
                onClick = { onItemSelected(index) },
                scrollPosition = listState.firstVisibleItemIndex,
                itemIndex = index
            )
        }
    }
}

@Composable
fun PickerItem(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    scrollPosition: Int,
    itemIndex: Int
) {
    val scale = remember { Animatable(1f) }

    LaunchedEffect(isSelected, scrollPosition) {
        val targetScale = if (isSelected) 2.5f else 1f
        scale.animateTo(
            if (isSelected || itemIndex == scrollPosition) targetScale else 1f,
            animationSpec = tween(durationMillis = 300)
        )
    }

    Box(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp)
            .scale(scale.value)
    ) {
        Text(
            text = text,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}