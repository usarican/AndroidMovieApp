package com.example.mymovieapp.core.services

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import java.util.UUID
import javax.inject.Inject

class DownloadWorkManager(
    @ApplicationContext private val context : Context,
) {
    fun startDownloadMovieGenres(){
        Timber.tag(TAG).d("startDownloadSessions called")

        val work = OneTimeWorkRequestBuilder<GenreDownloadWorker>()
            .addTag(DOWNLOAD_GENRE_WORK_TAG)
            .build()

        Timber.tag("SessionRepositoryBS").d(work.id.toString())

        val workManager = WorkManager.getInstance(context)
        workManager.enqueue(work)
    }

    companion object {
        private val TAG = DownloadWorkManager::class.java.simpleName
        private const val DOWNLOAD_GENRE_WORK_TAG = "DOWNLOAD_GENRE_WORK_TAG"
    }
}