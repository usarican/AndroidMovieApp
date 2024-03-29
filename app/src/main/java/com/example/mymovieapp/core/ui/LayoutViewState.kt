package com.example.mymovieapp.core.ui

import android.content.Context
import com.example.mymovieapp.R
import com.example.mymovieapp.core.ui.event.MyEventManager
import com.example.mymovieapp.core.ui.event.RetryNetworkCallEvent
import com.example.mymovieapp.features.home.ui.UserInterfaceState
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

class LayoutViewState (
    private val state : UserInterfaceState
) {
    fun isLoading() = state is UserInterfaceState.DisplayLoading

    fun isError() = state is UserInterfaceState.DisplayError

    fun isSuccess() = state is UserInterfaceState.DisplayUI

    fun getErrorImage(): Int? = if (state is UserInterfaceState.DisplayError) {
        when (state.error) {
            is IOException -> R.drawable.ic_no_connection
            else -> R.drawable.ic_error
        }
    } else {
        null
    }

    fun getErrorMessage(context: Context): String? =
        if (state is UserInterfaceState.DisplayError) {
            when (state.error) {
                is HttpException -> state.error.message()
                is SocketTimeoutException -> context.getString(R.string.timeout_error_message)
                is IOException -> context.getString(R.string.no_internet_connection)
                else -> context.getString(R.string.something_went_wrong)
            }
        } else {
            null
        }

    fun errorTryAgainFunc(){
        MyEventManager.sendEvent(RetryNetworkCallEvent())
    }
}