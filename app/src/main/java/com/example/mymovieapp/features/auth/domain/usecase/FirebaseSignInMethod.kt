package com.example.mymovieapp.features.auth.domain.usecase

import com.example.mymovieapp.core.data.State
import com.example.mymovieapp.features.auth.data.AuthRepository
import com.example.mymovieapp.features.auth.data.FirebaseFirestoreRepositoryImp
import com.example.mymovieapp.features.auth.data.FirebaseStorageRepositoryImp
import com.example.mymovieapp.features.auth.data.remote.UserDto
import com.example.mymovieapp.features.auth.data.remote.UserResponse
import com.example.mymovieapp.features.auth.domain.mapper.UserMapper
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageException
import javax.inject.Inject
import kotlin.jvm.Throws

class FirebaseSignInMethod @Inject constructor(
    private val firebaseStoreRepositoryImp: FirebaseFirestoreRepositoryImp,
    private val firebaseStorageRepositoryImp: FirebaseStorageRepositoryImp,
    private val authRepository: AuthRepository,
    private val userMapper: UserMapper
) {
    suspend fun firebaseSignInMethod(firebaseUser: FirebaseUser): UserResponse {
        val userFromFirestore =
            firebaseStoreRepositoryImp.getUserFromFirestore(firebaseUser.uid)
        val userResponse = userFromFirestore?.toObject<UserResponse>()
        return if (userResponse != null) {
            val userProfilePicture =
                firebaseStorageRepositoryImp.downloadUserImageFromFirebase(firebaseUser.uid)
            if (!userResponse.userEnteredFirstTime) {
                authRepository.insertUserToDatabase(
                    userMapper.responseToEntity(
                        userResponse,
                        userProfilePicture
                    )
                )
            } else {
                firebaseStoreRepositoryImp.updateUserEnteredFirstTimeParameter(
                    firebaseUser.uid,
                    false
                )
            }
            userResponse
        } else {
            val userDto = UserDto.Builder()
                .setUserEmail(firebaseUser.email ?: "empty@empty.com")
                .setUserUid(firebaseUser.uid)
                .build()
            firebaseStoreRepositoryImp.insertNewUser(userDto)
            userMapper.userDtoToUserResponse(userDto)
        }
    }
}
