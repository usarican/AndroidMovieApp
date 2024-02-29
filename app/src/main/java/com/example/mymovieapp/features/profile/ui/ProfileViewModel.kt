package com.example.mymovieapp.features.profile.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor() : ViewModel() {

    private val _showBottomSheet = MutableStateFlow(false)
    val bottomSheetState = _showBottomSheet.asStateFlow()

    private val _languageSelection = MutableStateFlow(true)

    fun changeBottomSheetState(state : Boolean){
        viewModelScope.launch {
            _showBottomSheet.emit(state)
            Timber.tag("Utku").d(_showBottomSheet.value.toString())
        }
    }
}