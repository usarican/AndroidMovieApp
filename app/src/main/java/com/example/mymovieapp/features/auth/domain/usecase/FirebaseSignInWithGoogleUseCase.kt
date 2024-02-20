package com.example.mymovieapp.features.auth.domain.usecase

import com.example.mymovieapp.core.data.State
import com.example.mymovieapp.features.auth.data.AuthRepository
import com.example.mymovieapp.features.auth.data.FirebaseAuthRepositoryImp
import com.example.mymovieapp.features.auth.data.FirebaseFirestoreRepositoryImp
import com.example.mymovieapp.features.auth.data.FirebaseStorageRepositoryImp
import com.example.mymovieapp.features.auth.data.remote.UserDto
import com.example.mymovieapp.features.auth.data.remote.UserResponse
import com.example.mymovieapp.features.auth.domain.mapper.UserMapper
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FirebaseSignInWithGoogleUseCase @Inject constructor(
    private val firebaseAuthRepositoryImp: FirebaseAuthRepositoryImp,
    private val firebaseFirestoreRepositoryImp: FirebaseFirestoreRepositoryImp,
    private val firebaseStorageRepositoryImp: FirebaseStorageRepositoryImp,
    private val authRepository: AuthRepository,
    private val userMapper: UserMapper
) {

    fun signInWithGoogle(idToken: String): Flow<State<UserResponse?>> = flow {
        emit(State.Loading)
        val authResult = firebaseAuthRepositoryImp.signInWithGoogle(idToken)
        val authUser = authResult.user
        authUser?.let { firebaseUser ->
            val fireStoreUser =
                firebaseFirestoreRepositoryImp.getUserFromFirestore(firebaseUser.uid)
            val userResponse = fireStoreUser?.toObject<UserResponse>()
            if (userResponse != null) {
                val userProfilePicture =
                    firebaseStorageRepositoryImp.downloadUserImageFromFirebase(authUser.uid)
                if (!userResponse.userEnteredFirstTime) {
                    authRepository.insertUserToDatabase(
                        userMapper.responseToEntity(
                            userResponse,
                            userProfilePicture
                        )
                    )
                } else {
                    firebaseFirestoreRepositoryImp.updateUserEnteredFirstTimeParameter(
                        authUser.uid,
                        false
                    )
                }
                emit(State.Success(userResponse))
            } else {
                val userDto = UserDto.Builder()
                    .setUserEmail(firebaseUser.email ?: "empty@empty.com")
                    .setUserUid(firebaseUser.uid)
                    .build()
                firebaseFirestoreRepositoryImp.insertNewUser(userDto)
                emit(State.Success(null))
            }
        } ?: emit(State.Error(Exception("Remote user is null.")))
    }
}