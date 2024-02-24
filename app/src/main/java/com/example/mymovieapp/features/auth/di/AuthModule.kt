package com.example.mymovieapp.features.auth.di

import com.example.mymovieapp.features.auth.data.AuthRepository
import com.example.mymovieapp.features.auth.data.AuthRepositoryImp
import com.example.mymovieapp.features.auth.data.FirebaseFirestoreRepository
import com.example.mymovieapp.features.auth.data.FirebaseFirestoreRepositoryImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class AuthModule {

    @Binds
    abstract fun bindAuthRepository(
        authRepositoryImp: AuthRepositoryImp
    ) : AuthRepository

    @Binds
    abstract fun bindFirebaseFirestoreRepository(
        firebaseFirestoreRepositoryImp: FirebaseFirestoreRepositoryImp
    ) : FirebaseFirestoreRepository
}