package com.example.mymovieapp.features.auth.domain.usecase

import com.example.mymovieapp.core.data.State
import com.example.mymovieapp.features.auth.data.FirebaseAuthRepositoryImp
import com.example.mymovieapp.features.auth.data.FirebaseFirestoreRepositoryImp
import com.example.mymovieapp.features.auth.data.remote.UserResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class FirebaseSignInWithEmailAndPasswordUseCase @Inject constructor(
    private val firebaseAuthRepositoryImp: FirebaseAuthRepositoryImp,
    private val firebaseStoreRepositoryImp: FirebaseFirestoreRepositoryImp
) {

    fun signInWithEmailAndPassword(userEmail : String, userPassword : String) : Flow<State<UserResponse?>> =
        flow {
            emit(State.Loading)
            val authResult = firebaseAuthRepositoryImp.createUser(userEmail,userPassword)
            val authUser = authResult.user
            authUser?.let {
                val userResponse = firebaseStoreRepositoryImp.getUserFromFirestore(authUser.uid).toObject(UserResponse::class.java)
                emit(State.Success(userResponse))
            } ?: emit(State.Error(Exception("Remote user is null.")))
        }.catch {
            emit(State.Error(it))
        }.flowOn(Dispatchers.IO)
}