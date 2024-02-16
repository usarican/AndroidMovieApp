package com.example.mymovieapp.features.auth.data.local

import com.example.mymovieapp.core.data.local.database.MovieAppDatabase
import com.example.mymovieapp.features.auth.data.local.entity.UserEntity
import javax.inject.Inject

class UserLocalDataSource @Inject constructor(
    database: MovieAppDatabase
) {
    private val userDatabase = database.userDatabase

    suspend fun insertNewUser(user : UserEntity) {
        userDatabase.insertNewUser(user)
    }

    suspend fun getUserWithUserUid(userUid : String) : UserEntity =
        userDatabase.getUserWithUserUid(userUid)

    suspend fun updateUser(user: UserEntity) {
        userDatabase.updateUser(user)
    }
}