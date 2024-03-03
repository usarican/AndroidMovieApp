package com.example.mymovieapp.features.profile.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mymovieapp.features.profile.data.titleTextAttributes
import com.example.mymovieapp.features.profile.ui.CustomContent
import kotlinx.coroutines.flow.collect
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun ProfileBottomSheet(
    closeCallBack: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    var selectedIndex by remember {
        mutableStateOf(0)
    }
    ModalBottomSheet(
        onDismissRequest = { closeCallBack() },
        sheetState = sheetState,
        modifier = Modifier.height(300.dp)
    ) {
        ProfileText(text = "Bottom Sheet", textAttributes = titleTextAttributes)
        /*PickerAnimationLazyColumn2(
            items = listOf(
                "A",
                "B",
            ),
            selectedItemIndex = selectedIndex,
            onItemSelected = {
                selectedIndex = it
            },
            limit = 10
        )*/
        CustomContent()
    }
}


@Composable
fun PickerAnimationLazyColumn2(
    items: List<String>,
    selectedItemIndex: Int,
    onItemSelected: (Int) -> Unit,
    limit: Int
) {
    val itemHeightPixels = remember { mutableStateOf(0) }
    val listState = rememberLazyListState()
    val scrollState = rememberScrollState()
    var scrollPosition by remember {
        mutableStateOf(0)
    }
    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxHeight()
            .padding(vertical = 45.dp)
    ) {
        itemsIndexed(items) { index, item ->
            val isSelected = index == selectedItemIndex
            PickerItem(
                text = item,
                isSelected = isSelected,
                onClick = { onItemSelected(index) },
                scrollPosition = scrollPosition,
                itemIndex = index,
                itemHeightCallBack = { size ->
                    itemHeightPixels.value = size
                }
            )
        }
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .collect { index ->
                Timber.tag("Utku").d("listState $index")
            }
    }
    LaunchedEffect(scrollState){
        snapshotFlow { scrollState.value }
            .collect {
                Timber.tag("Utku").d("Scroll State $it")
            }
    }
}

@Composable
fun PickerItem(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    scrollPosition: Int,
    itemIndex: Int,
    itemHeightCallBack: (Int) -> Unit,
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
            .onSizeChanged { size -> itemHeightCallBack(size.height) }
    ) {
        Text(
            text = text,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}


@Composable
private fun pixelsToDp(pixels: Int) = with(LocalDensity.current) { pixels.toDp() }

