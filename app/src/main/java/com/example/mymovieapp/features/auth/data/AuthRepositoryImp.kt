package com.example.mymovieapp.features.auth.data

import com.example.mymovieapp.features.auth.data.local.UserLocalDataSource
import com.example.mymovieapp.features.auth.data.local.entity.UserEntity
import javax.inject.Inject

class AuthRepositoryImp @Inject constructor(
    private val userLocalDataSource: UserLocalDataSource
) : AuthRepository {
    override suspend fun getUserDataFromDatabase(userUid : String): UserEntity {
        return userLocalDataSource.getUserWithUserUid(userUid)
    }

    override suspend fun insertUserToDatabase(user: UserEntity) {
        userLocalDataSource.insertNewUser(user)
    }

    override suspend fun updateUserInDatabase(user: UserEntity) {
        userLocalDataSource.updateUser(user)
    }
}