package com.example.mymovieapp.core.di

import android.content.Context
import androidx.room.Room
import com.example.mymovieapp.core.data.local.database.MovieAppDatabase
import com.example.mymovieapp.core.services.DownloadWorkManager
import com.example.mymovieapp.features.explore.ui.dialog.MovieFilterUtils
import com.example.mymovieapp.utils.JsonConverter
import com.example.mymovieapp.utils.NetworkUtils
import com.example.mymovieapp.utils.PathHelper
import com.example.mymovieapp.utils.StringProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Singleton
    @Provides
    fun provideStringResource(@ApplicationContext context: Context) =
        StringProvider(context)

    @Singleton
    @Provides
    fun providePathHelper(
        @ApplicationContext context: Context,
        jsonConverter: JsonConverter
    ) : PathHelper =
        PathHelper(context,jsonConverter)
    @Singleton
    @Provides
    fun provideJsonConverter() : JsonConverter = JsonConverter()

    @Provides
    fun provideNetworkUtils(@ApplicationContext context: Context) : NetworkUtils =
        NetworkUtils(context)

    @Provides
    fun provideMovieFilterUtils(stringProvider: StringProvider) : MovieFilterUtils =
        MovieFilterUtils(stringProvider)

    @Singleton
    @Provides
    fun provideMovieAppDatabase(
        @ApplicationContext context : Context
    ) : MovieAppDatabase {
        val room = Room.databaseBuilder(
            context,
            MovieAppDatabase::class.java,
            "db-movie-app"
        )

        room.fallbackToDestructiveMigration()
        return room.build()
    }

    @Provides
    fun provideDownloadWorkerManager(
        @ApplicationContext context: Context
    ) : DownloadWorkManager = DownloadWorkManager(
        context = context
    )
}