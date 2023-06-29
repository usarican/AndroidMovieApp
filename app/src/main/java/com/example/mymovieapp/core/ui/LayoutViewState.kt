package com.example.mymovieapp.core.ui

import android.content.Context
import com.example.mymovieapp.R
import com.example.mymovieapp.core.data.State
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

class LayoutViewState(
    private val state : State<*>
) {
    fun isLoading() = state is State.Loading

    fun isError() = state is State.Error

    fun isSuccess() = state is State.Success

    fun getErrorImage(): Int? = if (state is State.Error) {
        when (state.exception) {
            is IOException -> 0
            else -> 0
        }
    } else {
        null
    }

    fun getErrorMessage(context: Context): String? =
        if (state is State.Error) {
            when (state.exception) {
                is HttpException -> state.exception.message()
                is SocketTimeoutException -> context.getString(R.string.timeout_error_message)
                is IOException -> context.getString(R.string.no_internet_connection)
                else -> context.getString(R.string.something_went_wrong)
            }
        } else {
            null
        }
}