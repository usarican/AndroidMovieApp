package com.example.mymovieapp.features.auth.domain.usecase

import com.example.mymovieapp.core.data.State
import com.example.mymovieapp.features.auth.data.AuthRepository
import com.example.mymovieapp.features.auth.data.FirebaseFirestoreRepositoryImp
import com.example.mymovieapp.features.auth.data.local.entity.UserEntity
import com.example.mymovieapp.features.auth.domain.mapper.GenreMapper
import com.example.mymovieapp.features.auth.domain.mapper.UserMapper
import com.example.mymovieapp.features.auth.domain.model.AuthUserDataStateModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class FirebaseUpdateUserInformationUseCase @Inject constructor(
    private val firebaseFirestoreRepositoryImp: FirebaseFirestoreRepositoryImp,
    private val authRepository: AuthRepository,
    private val userMapper: UserMapper
) {

    fun updateUser(authUserDataStateModel: AuthUserDataStateModel): Flow<State<UserEntity>> =
        flow {
            emit(State.Loading)
            val updateInformation =
                userMapper.authUserDataStateModelToFirebaseUserModel(authUserDataStateModel)
            authUserDataStateModel.userUid?.let {
                firebaseFirestoreRepositoryImp.updateUserAfterProfileSetupSection(
                    it,
                    updateInformation
                )
                val userEntity =
                    userMapper.authUserDataStateModelToUserEntity(authUserDataStateModel)
                authRepository.insertUserToDatabase(userEntity)
                emit(State.Success(userEntity))
            } ?: emit(State.Error(Exception("User Uid is Null.")))
        }.catch {
            emit(State.Error(it))
        }.flowOn(Dispatchers.IO)
}