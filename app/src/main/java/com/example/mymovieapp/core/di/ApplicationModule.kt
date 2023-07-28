package com.example.mymovieapp.core.di

import android.content.Context
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
    fun provideStringResource(@ApplicationContext context: Context) : StringProvider =
        StringProvider(context)
}