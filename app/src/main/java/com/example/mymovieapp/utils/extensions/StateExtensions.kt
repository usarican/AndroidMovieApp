package com.example.mymovieapp.utils.extensions

import com.example.mymovieapp.core.data.State
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform

fun <T, R> State<T>.map(transform: (T) -> R): State<R> {
    return when (this) {
        is State.Success -> State.Success(transform(data))
        is State.Error -> State.Error(exception)
        is State.Loading -> State.Loading
    }
}

suspend fun <T> State<T>.converter(
    doWhenStateSuccess: (suspend (_: T) -> Unit)? = null,
    doWhenStateError: ((_: Throwable) -> Unit)? = null,
    doWhenStateLoading: (() -> Unit)? = null
) {
    when (this) {
        is State.Success -> if (doWhenStateSuccess != null) {
            doWhenStateSuccess(data)
        }

        is State.Error -> if (doWhenStateError != null) {
            doWhenStateError(exception)
        }

        is State.Loading -> if (doWhenStateLoading != null) {
            doWhenStateLoading()
        }
    }
}

fun <T> Flow<State<T>>.doOnSuccess(action: suspend (T) -> Unit): Flow<State<T>> =
    transform { value ->
        if (value is State.Success) {
            action(value.data)
        }
        return@transform emit(value)
    }

fun <T> Flow<State<T>>.doOnError(action: suspend (Throwable) -> Unit): Flow<State<T>> =
    transform { value ->
        if (value is State.Error) {
            action(value.exception)
        }
        return@transform emit(value)
    }

fun <T> Flow<State<T>>.doOnLoading(action: suspend () -> Unit): Flow<State<T>> =
    transform { value ->
        if (value is State.Loading) {
            action()
        }
        return@transform emit(value)
    }