package com.example.mymovieapp.core.di

import android.content.Context
import androidx.fragment.app.FragmentActivity
import com.example.mymovieapp.core.ui.LayoutViewState
import com.example.mymovieapp.features.dialog.LoadingDialog
import com.example.mymovieapp.features.explore.ui.dialog.MovieFilterUtils
import com.example.mymovieapp.utils.NetworkUtils
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

    @Provides
    fun provideNetworkUtils(@ApplicationContext context: Context) : NetworkUtils =
        NetworkUtils(context)

    @Provides
    fun provideMovieFilterUtils(stringProvider: StringProvider) : MovieFilterUtils =
        MovieFilterUtils(stringProvider)
}