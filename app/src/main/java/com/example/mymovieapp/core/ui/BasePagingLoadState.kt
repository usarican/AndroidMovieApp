package com.example.mymovieapp.core.ui

import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState


abstract class BasePagingLoadState {

    protected fun handlePagingLoadState(
        loadState : CombinedLoadStates,
        doOnLoading : () -> Unit,
        doOnSuccess : () -> Unit,
        doOnError : (error : Throwable) -> Unit
    ) {
        when(loadState.refresh){
            is LoadState.Loading -> doOnLoading.invoke()
            is LoadState.NotLoading -> doOnSuccess.invoke()
            is LoadState.Error -> doOnError.invoke((loadState.refresh as LoadState.Error).error)
        }
    }
}