package com.example.mymovieapp.features.auth.data

import com.example.mymovieapp.core.data.State
import com.example.mymovieapp.features.auth.data.remote.UserDto
import kotlinx.coroutines.flow.Flow

interface FirebaseFirestoreRepository {
    fun insertNewUser(userDto: UserDto) : Flow<State<Nothing?>>
    fun getUserFromFirestore() : UserDto
}