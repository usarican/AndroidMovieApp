package com.example.mymovieapp.features.auth.data

import com.example.mymovieapp.core.data.BaseRepository
import com.example.mymovieapp.features.auth.ui.LoginFragment
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await
import timber.log.Timber
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

    override suspend fun signInWithGoogle(idToken: String): AuthResult {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        return auth.signInWithCredential(credential).await()
    }

    override suspend fun signInWithFacebook(idToken: String): AuthResult {
        val credential = FacebookAuthProvider.getCredential(idToken)
        return auth.signInWithCredential(credential).await()
    }

    override fun signOut() {
        auth.signOut()
    }

    companion object {
        private val TAG = FirebaseAuthRepositoryImp::class.java.simpleName
    }
}