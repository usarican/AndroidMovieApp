package com.example.mymovieapp.core.services

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.mymovieapp.core.data.MovieGenreRepositoryImp
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
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
            genreRepository.insertGenreListFromRemoteToDatabase("en")
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