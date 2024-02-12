package com.example.mymovieapp.features.auth.data

import com.example.mymovieapp.core.data.State
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface FirebaseAuthRepository {
    fun createUser(userEmail : String, userPassword : String) : Flow<State<FirebaseUser?>>
    fun signIn(userEmail : String, userPassword : String)
    fun signOut()

}