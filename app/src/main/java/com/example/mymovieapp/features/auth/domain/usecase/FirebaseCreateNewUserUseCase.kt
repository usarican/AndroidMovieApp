package com.example.mymovieapp.features.auth.domain.usecase

import com.example.mymovieapp.core.data.State
import com.example.mymovieapp.features.auth.data.FirebaseAuthRepositoryImp
import com.example.mymovieapp.features.auth.data.FirebaseFirestoreRepositoryImp
import com.example.mymovieapp.features.auth.data.remote.UserDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.lang.Exception
import javax.inject.Inject

class FirebaseCreateNewUserUseCase @Inject constructor(
    private val firebaseAuthRepositoryImp: FirebaseAuthRepositoryImp,
    private val firebaseFirestoreRepositoryImp: FirebaseFirestoreRepositoryImp
) {
    fun createNewUser(email: String, password: String): Flow<State<UserDto?>> =
        flow {
            emit(State.Loading)
            val user = firebaseAuthRepositoryImp.createUser(email, password).user
            user?.let {
                val userDto = UserDto.Builder()
                    .setUserEmail(user.email ?: email)
                    .setUserUid(user.uid)
                    .build()
                firebaseFirestoreRepositoryImp.insertNewUser(userDto)
                emit(State.Success(userDto))
            } ?: emit(State.Error(Exception("Firebase User is Null")))
        }.catch {
            emit(State.Error(it))
        }.flowOn(Dispatchers.IO)
}