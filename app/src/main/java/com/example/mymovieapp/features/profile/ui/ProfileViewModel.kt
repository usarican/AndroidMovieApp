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

    private val _counter = MutableStateFlow(0)
    val counterState = _counter.asStateFlow()

    fun incrementCounter(){
        viewModelScope.launch {
            _counter.emit(_counter.value + 1)
            Timber.tag("Utku").d(_counter.value.toString())
        }
    }

    suspend fun incrementCounterSuspend(){
        _counter.emit(_counter.value++)
    }
}