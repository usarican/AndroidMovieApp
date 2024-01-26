package com.example.mymovieapp

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.example.mymovieapp.core.data.remote.repository.MovieGenreRepositoryImp
import com.example.mymovieapp.core.services.DownloadWorkManager
import com.example.mymovieapp.core.services.GenreDownloadWorker
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class MovieApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var customWorkerFactory: HiltWorkerFactory
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder().setWorkerFactory(customWorkerFactory).build()
    }

    class CustomWorkerFactory @Inject constructor(
        private val genreRepositoryImp: MovieGenreRepositoryImp
    ) : WorkerFactory(){
        override fun createWorker(
            appContext: Context,
            workerClassName: String,
            workerParameters: WorkerParameters
        ): ListenableWorker? = GenreDownloadWorker(
            appContext,workerParameters,genreRepositoryImp
        )

    }


}