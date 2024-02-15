package com.example.mymovieapp.features.auth.data

import com.example.mymovieapp.core.data.State
import com.example.mymovieapp.features.auth.data.remote.UserDto
import com.example.mymovieapp.utils.Constants.FIREBASE_FIRESTORE_USER_COLLECTION_NAME
import com.google.firebase.firestore.DocumentSnapshot
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
            .collection(FIREBASE_FIRESTORE_USER_COLLECTION_NAME)
            .document(userDto.userUid ?: UUID.randomUUID().toString())
            .set(userDto)
            .await()
    }


    override suspend fun getUserFromFirestore(remoteUserUid : String): DocumentSnapshot {
        return firestore
            .collection(FIREBASE_FIRESTORE_USER_COLLECTION_NAME)
            .document(remoteUserUid)
            .get()
            .await()
    }
}