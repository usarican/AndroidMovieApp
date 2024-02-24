package com.example.mymovieapp.features.auth.data

import com.example.mymovieapp.features.auth.data.local.entity.UserEntity

interface AuthRepository {
    suspend fun getUserDataFromDatabase(userUid : String) : UserEntity?
    suspend fun insertUserToDatabase(user : UserEntity)

    suspend fun updateUserInDatabase(user : UserEntity)
}