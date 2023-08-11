package com.example.mymovieapp.core.di

import androidx.fragment.app.FragmentActivity
import com.example.mymovieapp.features.dialog.LoadingDialog
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object ActivityModule {

    @Provides
    fun provideLoadingDialog(
        activity : FragmentActivity
    ) : LoadingDialog = LoadingDialog(activity)
}