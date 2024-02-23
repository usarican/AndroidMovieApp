package com.example.mymovieapp.features.auth.data

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult

interface FirebaseAuthRepository {
    suspend fun createUser(userEmail : String, userPassword : String) : AuthResult
    suspend fun signIn(userEmail : String, userPassword : String) : AuthResult

    suspend fun signInWithGoogle(idToken: String) : AuthResult
    suspend fun signInWithFacebook(idToken: String) : AuthResult
    fun signOut()

}