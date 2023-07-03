package com.example.mymovieapp.features.home.di

import com.example.mymovieapp.features.home.data.HomeRepository
import com.example.mymovieapp.features.home.data.HomeRepositoryImp
import com.example.mymovieapp.features.home.data.remote.HomeService
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import retrofit2.Retrofit


@Module
@InstallIn(ViewModelComponent::class)
abstract class HomeModule {

    @Binds
    abstract fun bindHomeRepository(
        homeRepositoryImp: HomeRepositoryImp
    ) : HomeRepository

    companion object {
        @Provides
        fun provideHomeService(retrofit: Retrofit) : HomeService =
            retrofit.create(HomeService::class.java)
    }

}
