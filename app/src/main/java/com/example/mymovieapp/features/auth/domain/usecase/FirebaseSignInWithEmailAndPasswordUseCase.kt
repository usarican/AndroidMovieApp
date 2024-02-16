package com.example.mymovieapp.features.auth.domain.usecase

import com.example.mymovieapp.core.data.State
import com.example.mymovieapp.features.auth.data.AuthRepository
import com.example.mymovieapp.features.auth.data.FirebaseAuthRepositoryImp
import com.example.mymovieapp.features.auth.data.FirebaseFirestoreRepositoryImp
import com.example.mymovieapp.features.auth.data.remote.UserResponse
import com.example.mymovieapp.features.auth.domain.mapper.UserMapper
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import javax.inject.Inject

class FirebaseSignInWithEmailAndPasswordUseCase @Inject constructor(
    private val firebaseAuthRepositoryImp: FirebaseAuthRepositoryImp,
    private val firebaseStoreRepositoryImp: FirebaseFirestoreRepositoryImp,
    private val authRepository: AuthRepository,
    private val userMapper: UserMapper
) {

    fun signInWithEmailAndPassword(userEmail : String, userPassword : String) : Flow<State<UserResponse?>> =
        flow {
            emit(State.Loading)
            val authResult = firebaseAuthRepositoryImp.signIn(userEmail,userPassword)
            val authUser = authResult.user
            authUser?.let {
                val userFromFirestore =
                    firebaseStoreRepositoryImp.getUserFromFirestore(authUser.uid)
                Timber.tag("Utku").d(userFromFirestore.data.toString())
                val userResponse = userFromFirestore.toObject<UserResponse>()
                if (userResponse != null) {
                    if (!userResponse.userEnteredFirstTime) {
                        authRepository.insertUserToDatabase(userMapper.responseToEntity(userResponse))
                    } else {
                        firebaseStoreRepositoryImp.updateUserEnteredFirstTimeParameter(authUser.uid,false)
                    }
                } else {
                    emit(State.Error(Exception("Firestore user is null.")))
                }
                emit(State.Success(userResponse))
            } ?: emit(State.Error(Exception("Remote user is null.")))
        }.catch {
            emit(State.Error(it))
        }.flowOn(Dispatchers.IO)
}