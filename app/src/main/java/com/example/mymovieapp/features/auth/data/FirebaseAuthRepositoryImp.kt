package com.example.mymovieapp.features.auth.data

import com.example.mymovieapp.core.data.BaseRepository
import com.example.mymovieapp.core.data.State
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

class FirebaseAuthRepositoryImp @Inject constructor(
    private val auth : FirebaseAuth
) : FirebaseAuthRepository, BaseRepository(){
    override fun createUser(userEmail: String, userPassword: String): Flow<State<FirebaseUser>> {
        return flow {
            emit(State.Loading)
            val authResult = auth.createUserWithEmailAndPassword(userEmail,userPassword).await()
            authResult.user?.let { firebaseUser ->
                emit(State.Success(firebaseUser))
            }
        }.catch { error ->
            error.printStackTrace()
            emit(State.Error(error))
        }.flowOn(Dispatchers.IO)
    }

    override fun signIn(userEmail: String, userPassword: String) {
        TODO("Not yet implemented")
    }

    override fun signOut() {
        TODO("Not yet implemented")
    }

    companion object {
        private val TAG = FirebaseAuthRepositoryImp::class.java.simpleName
    }
}