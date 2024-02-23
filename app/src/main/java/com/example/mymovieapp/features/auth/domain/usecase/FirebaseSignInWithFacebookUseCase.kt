package com.example.mymovieapp.features.auth.domain.usecase

import com.example.mymovieapp.core.data.State
import com.example.mymovieapp.features.auth.data.FirebaseAuthRepositoryImp
import com.example.mymovieapp.features.auth.data.remote.UserResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class FirebaseSignInWithFacebookUseCase @Inject constructor(
    private val firebaseAuthRepositoryImp: FirebaseAuthRepositoryImp,
    private val firebaseSignInMethod: FirebaseSignInMethod
) {
    fun signInWithFacebook(idToken: String): Flow<State<UserResponse?>> = flow {
        emit(State.Loading)
        val authResult = firebaseAuthRepositoryImp.signInWithFacebook(idToken)
        val authUser = authResult.user
        authUser?.let { firebaseUser ->
            val user = firebaseSignInMethod.firebaseSignInMethod(firebaseUser)
            emit(State.Success(user))
        } ?: emit(State.Error(Exception("Remote user is null.")))
    }.catch {
        emit(State.Error(it))
    }.flowOn(Dispatchers.IO)
}