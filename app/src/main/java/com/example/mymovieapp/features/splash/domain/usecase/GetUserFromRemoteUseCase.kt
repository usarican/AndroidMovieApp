package com.example.mymovieapp.features.splash.domain.usecase

import com.example.mymovieapp.core.data.State
import com.example.mymovieapp.features.auth.data.AuthRepository
import com.example.mymovieapp.features.auth.data.FirebaseFirestoreRepository
import com.example.mymovieapp.features.auth.data.FirebaseStorageRepositoryImp
import com.example.mymovieapp.features.auth.data.remote.UserResponse
import com.example.mymovieapp.features.auth.domain.mapper.UserMapper
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetUserFromRemoteUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val firebaseFirestoreRepository: FirebaseFirestoreRepository,
    private val firebaseStorageRepositoryImp: FirebaseStorageRepositoryImp,
    private val userMapper: UserMapper
) {
    fun getUserInformation(userUid: String?): Flow<State<UserResponse?>> = flow {
        emit(State.Loading)
        userUid?.let {
            val localUser = authRepository.getUserDataFromDatabase(userUid)
            if (localUser != null) {
                emit(State.Success(userMapper.entityToResponse(localUser)))
            } else {
                val userResponse = firebaseFirestoreRepository.getUserFromFirestore(userUid)
                    ?.toObject<UserResponse>()
                val userImage = firebaseStorageRepositoryImp.downloadUserImageFromFirebase(userUid)
                userResponse?.let {
                    val userEntity = userMapper.responseToEntity(userResponse, userImage)
                    authRepository.insertUserToDatabase(userEntity)
                    emit(State.Success(userResponse))
                } ?: emit(State.Error(Exception("User Response is could not fetch from Remote.")))
            }
        } ?: emit(State.Error(Exception("User Uid is could not be null.")))

    }.catch {
        emit(State.Error(it))
    }.flowOn(Dispatchers.IO)
}