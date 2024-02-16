package com.example.mymovieapp.features.auth.data

import com.google.firebase.auth.AuthResult

interface FirebaseAuthRepository {
    suspend fun createUser(userEmail : String, userPassword : String) : AuthResult
    suspend fun signIn(userEmail : String, userPassword : String) : AuthResult
    fun signOut()

}