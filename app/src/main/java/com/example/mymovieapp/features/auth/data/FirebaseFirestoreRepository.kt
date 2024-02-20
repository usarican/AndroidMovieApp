package com.example.mymovieapp.features.auth.data

import com.example.mymovieapp.core.data.State
import com.example.mymovieapp.features.auth.data.remote.UserDto
import com.example.mymovieapp.features.auth.domain.model.AuthUserDataStateModel
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.flow.Flow

interface FirebaseFirestoreRepository {
    suspend fun insertNewUser(userDto: UserDto)
    suspend fun getUserFromFirestore(remoteUserUid : String) : DocumentSnapshot?
    suspend fun updateUserEnteredFirstTimeParameter(remoteUserUid : String,userEnteredFirstTime: Boolean)
    suspend fun updateUserAfterProfileSetupSection(remoteUserUid : String,updateInformation : Map<String,Any?>)
}