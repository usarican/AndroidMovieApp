package com.example.mymovieapp.core.di

import com.example.mymovieapp.core.data.remote.MovieGenreService
import com.example.mymovieapp.core.data.remote.RequestInterceptor
import com.example.mymovieapp.utils.Constants.MOVIE_API_URL
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideOkHttpClient() : OkHttpClient {
        return OkHttpClient().newBuilder()
            .addNetworkInterceptor(RequestInterceptor())
            .readTimeout(15,TimeUnit.SECONDS)
            .connectTimeout(15,TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun provideMoshi() : Moshi {
        return Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    }

    @Singleton
    @Provides
    fun provideMoshiConverterFactory(moshi: Moshi) : MoshiConverterFactory {
        return MoshiConverterFactory.create(moshi)
    }

    @Singleton
    @Provides
    fun provideMovieApiUrl() : String = MOVIE_API_URL

    @Singleton
    @Provides
    fun provideRetrofit(
        movieApiUrl : String,
        okHttpClient: OkHttpClient,
        moshiConverterFactory: MoshiConverterFactory
    ) : Retrofit {
        return Retrofit.Builder()
            .baseUrl(movieApiUrl)
            .client(okHttpClient)
            .addConverterFactory(moshiConverterFactory)
            .build()
    }

    @Singleton
    @Provides
    fun provideMovieGenreService(retrofit: Retrofit) : MovieGenreService {
        return retrofit.create(MovieGenreService::class.java)
    }

    @Singleton
    @Provides
    fun provideFirebaseAuth() : FirebaseAuth = Firebase.auth

    @Singleton
    @Provides
    fun provideFirebaseFirestore() : FirebaseFirestore = Firebase.firestore
}