package com.example.mymovieapp.features.explore.di

import com.example.mymovieapp.features.explore.data.MovieExploreRepository
import com.example.mymovieapp.features.explore.data.MovieExploreRepositoryImp
import com.example.mymovieapp.features.explore.data.remote.MovieSearchService
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
abstract class ExploreModule {
    @Binds
    abstract fun bindMovieExploreRepository(
        movieExploreRepositoryImp: MovieExploreRepositoryImp
    ) : MovieExploreRepository

    companion object {

        @Provides
        fun provideMovieSearchService(retrofit: Retrofit) : MovieSearchService =
            retrofit.create(MovieSearchService::class.java)
    }
}