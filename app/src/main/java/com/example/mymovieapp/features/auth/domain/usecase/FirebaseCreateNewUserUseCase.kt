package com.example.mymovieapp.features.auth.domain.usecase

import com.example.mymovieapp.core.data.State
import com.example.mymovieapp.features.auth.data.FirebaseAuthRepositoryImp
import com.example.mymovieapp.features.auth.data.FirebaseFirestoreRepositoryImp
import com.example.mymovieapp.features.auth.data.remote.UserDto
import com.example.mymovieapp.features.auth.domain.mapper.UserMapper
import com.example.mymovieapp.features.auth.domain.model.User
import com.example.mymovieapp.utils.extensions.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FirebaseCreateNewUserUseCase @Inject constructor(
    private val firebaseAuthRepositoryImp: FirebaseAuthRepositoryImp,
    private val firebaseFirestoreRepositoryImp: FirebaseFirestoreRepositoryImp,
    private val userMapper: UserMapper
) {
    fun createNewUser(email : String, password : String) : Flow<State<UserDto>> {
        return firebaseAuthRepositoryImp.createUser(email,password).map { state ->
            state.map { firebaseUser ->
                val userDto = UserDto.Builder()
                    .setUserEmail(firebaseUser.email ?: email)
                    .setUserUid(firebaseUser.uid)
                    .build()
                userDto
            }
        }
    }
}