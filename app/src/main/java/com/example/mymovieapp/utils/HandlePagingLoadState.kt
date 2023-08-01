package com.example.mymovieapp.utils

import androidx.paging.PagingDataAdapter
import com.example.mymovieapp.core.ui.BasePagingLoadState
import com.example.mymovieapp.features.home.domain.model.CategoryType

class HandlePagingLoadState(
    private val adapter : PagingDataAdapter<*,*>,
    private val doOnLoading : () -> Unit,
    private val doOnSuccess : () -> Unit,
    private val doOnError : (error : Throwable) -> Unit
) : BasePagingLoadState() {

    init {
        adapter.addLoadStateListener { combinedLoadStates ->
            handlePagingLoadState(
                loadState = combinedLoadStates,
                doOnLoading = doOnLoading,
                doOnSuccess = doOnSuccess,
                doOnError = doOnError
            )
        }
    }
}

interface PagingLoadStateCallBack {
    fun doOnLoading(categoryType: CategoryType)
    fun doOnSuccess(categoryType: CategoryType)
    fun doOnError(error : Throwable, categoryType: CategoryType)
}