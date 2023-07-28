package com.example.mymovieapp.utils

import com.example.mymovieapp.core.data.State
import com.example.mymovieapp.features.home.ui.HomeFragment
import com.example.mymovieapp.utils.extensions.doOnError
import com.example.mymovieapp.utils.extensions.doOnLoading
import com.example.mymovieapp.utils.extensions.doOnSuccess
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import timber.log.Timber

class AsyncTaskExample {
    suspend fun apiCall1() : String {
        delay(4000L)
        return "API CALL 1"
    }

    suspend fun apiCall2() : String {
        delay(2000L)
        return "API CALL 2"
    }

    data class Response(
        val response1 : String,
        val response2 : String
    )

    private fun allNetworkCall() : Flow<State<Response>> = flow {
        emit(State.Loading)
        coroutineScope {
            val response1 = async { apiCall1() }
            val response2 = async { apiCall2() }
            emit(State.Success(Response(response1.await(),response2.await())))
        }
    }.catch {  emit(State.Error(it))}.
    flowOn(Dispatchers.IO)

    private fun run() {
        allNetworkCall().
        doOnLoading {
            Timber.tag(TAG).d("Loading..")
        }.
        doOnSuccess {
            Timber.tag(TAG).d("Response 1 : ${it.response1} Response 2 : ${it.response2}")
        }.
        doOnError { Timber.tag(TAG).d(it) }.
        onEach {
            Timber.tag(TAG).d("State $it")
        }.launchIn(CoroutineScope(Dispatchers.IO))
    }

    companion object {
        const val TAG = "A"
    }

}