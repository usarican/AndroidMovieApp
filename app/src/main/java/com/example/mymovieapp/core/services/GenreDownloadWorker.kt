package com.example.mymovieapp.core.services

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.mymovieapp.core.data.State
import com.example.mymovieapp.core.data.local.database.MovieAppDatabase
import com.example.mymovieapp.core.data.remote.repository.MovieGenreRepository
import com.example.mymovieapp.core.data.remote.repository.MovieGenreRepositoryImp
import com.example.mymovieapp.utils.extensions.doOnError
import com.example.mymovieapp.utils.extensions.doOnLoading
import com.example.mymovieapp.utils.extensions.doOnSuccess
import com.example.mymovieapp.utils.extensions.map
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.lang.Exception

@HiltWorker
class GenreDownloadWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val genreRepository : MovieGenreRepositoryImp
):  CoroutineWorker(appContext,workerParams){
    override suspend fun doWork(): Result {
        Timber.tag(TAG).d("Genre Download Worker is Working..")
        return try {
            genreRepository.getMovieGenreList("en")
            return Result.success()
        } catch (e : Exception) {
            Timber.tag(TAG).d(e)
            Result.failure()
        }
    }

    companion object {
        private val TAG = GenreDownloadWorker::class.java.simpleName
    }
}

class CustomWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context,params){

    val flow = flow {
        emit(State.Loading)
        delay(1000L)
        emit(State.Success(10))
    }

    override suspend fun doWork(): Result {
        Timber.tag("Utku").d("Simple Worker Working...")
        var result : ListenableWorker.Result = Result.failure()
        flow.collectLatest { state ->
            when(state){
                is State.Error -> {
                    Timber.tag("Utku").d("Flow Error")
                    result = Result.failure()
                }
                State.Loading -> {
                    Timber.tag("Utku").d("Flow Loading")
                    result = Result.success()
                }
                is State.Success -> {
                    Timber.tag("Utku").d("Flow Success data is ${state.data}")
                }
            }
        }
        return result
    }


}