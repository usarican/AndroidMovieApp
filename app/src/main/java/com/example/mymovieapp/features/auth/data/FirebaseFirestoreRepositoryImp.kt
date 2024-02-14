package com.example.mymovieapp.features.auth.data

import com.example.mymovieapp.core.data.State
import com.example.mymovieapp.features.auth.data.remote.UserDto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

class FirebaseFirestoreRepositoryImp @Inject constructor(
    private val firestore: FirebaseFirestore
) : FirebaseFirestoreRepository {
    override suspend fun insertNewUser(userDto: UserDto) {
        firestore
            .collection("users")
            .document(userDto.userUid ?: UUID.randomUUID().toString())
            .set(userDto)
            .await()
    }


    override fun getUserFromFirestore(): UserDto {
        TODO("Not yet implemented")
    }
}