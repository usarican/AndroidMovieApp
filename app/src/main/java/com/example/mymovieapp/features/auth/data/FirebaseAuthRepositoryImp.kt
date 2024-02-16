package com.example.mymovieapp.features.auth.data

import com.example.mymovieapp.core.data.BaseRepository
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseAuthRepositoryImp @Inject constructor(
    private val auth : FirebaseAuth
) : FirebaseAuthRepository, BaseRepository(){
    override suspend fun createUser(userEmail: String, userPassword: String) : AuthResult {
        return auth.createUserWithEmailAndPassword(userEmail,userPassword).await()
    }

    override suspend fun signIn(userEmail: String, userPassword: String): AuthResult {
        return auth.signInWithEmailAndPassword(userEmail,userPassword).await()
    }

    override fun signOut() {
        TODO("Not yet implemented")
    }

    companion object {
        private val TAG = FirebaseAuthRepositoryImp::class.java.simpleName
    }
}