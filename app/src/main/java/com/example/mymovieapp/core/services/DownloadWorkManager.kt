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

        val work = OneTimeWorkRequestBuilder<GenreDownloadWorker>()
            .addTag(DOWNLOAD_GENRE_WORK_TAG)
            .build()

        val workManager = WorkManager.getInstance(context)
        workManager.enqueue(work)
    }

    companion object {
        private const val DOWNLOAD_GENRE_WORK_TAG = "DOWNLOAD_GENRE_WORK_TAG"
    }
}