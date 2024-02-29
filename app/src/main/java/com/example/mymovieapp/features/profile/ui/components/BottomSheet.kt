package com.example.mymovieapp.features.profile.ui.components

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import com.example.mymovieapp.features.profile.data.titleTextAttributes

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun ProfileBottomSheet(
    closeCallBack : () -> Unit
){
    val sheetState = rememberModalBottomSheetState()
    ModalBottomSheet(
        onDismissRequest = { closeCallBack() },
        sheetState = sheetState
    ) {
        ProfileText(text = "Bottom Sheet", textAttributes = titleTextAttributes)
    }
}