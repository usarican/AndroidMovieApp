package com.example.mymovieapp.features.details.di

import com.example.mymovieapp.features.details.data.MovieDetailsRepository
import com.example.mymovieapp.features.details.data.MovieDetailsRepositoryImp
import com.example.mymovieapp.features.details.data.remote.MovieDetailService
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@InstallIn(ViewModelComponent::class)
@Module
abstract class DetailModule {

    @Binds
    abstract fun bindMovieDetailRepository(
        detailsRepositoryImp: MovieDetailsRepositoryImp
    ) : MovieDetailsRepository

    companion object {
      @Singleton
      @Provides
      fun provideMovieDetailService(retrofit: Retrofit) : MovieDetailService =
          retrofit.create(MovieDetailService::class.java)
    }
}