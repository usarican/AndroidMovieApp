package com.example.mymovieapp.features.auth.data

import com.example.mymovieapp.features.auth.data.remote.UserDto
import com.example.mymovieapp.utils.Constants.FIREBASE_FIRESTORE_USER_COLLECTION_NAME
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
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


    override suspend fun getUserFromFirestore(remoteUserUid : String): DocumentSnapshot? {
        return firestore
            .collection(FIREBASE_FIRESTORE_USER_COLLECTION_NAME)
            .document(remoteUserUid)
            .get()
            .await()
        /*return try {
            firestore
                .collection(FIREBASE_FIRESTORE_USER_COLLECTION_NAME)
                .document(remoteUserUid)
                .get()
                .await()
        } catch (e : FirebaseFirestoreException) {
            if (e.code == FirebaseFirestoreException.Code.NOT_FOUND) {
                null
            } else {
                throw e
            }
        }*/
    }

    override suspend fun updateUserEnteredFirstTimeParameter(remoteUserUid : String,userEnteredFirstTime: Boolean) {
        val updates = mapOf<String,Any>(
            "userEnteredFirstTime" to userEnteredFirstTime
        )
        firestore
            .collection(FIREBASE_FIRESTORE_USER_COLLECTION_NAME)
            .document(remoteUserUid)
            .update(updates)
            .await()
    }

    override suspend fun updateUserAfterProfileSetupSection(
        remoteUserUid: String,
        updateInformation : Map<String,Any?>
    ) {
        firestore
            .collection(FIREBASE_FIRESTORE_USER_COLLECTION_NAME)
            .document(remoteUserUid)
            .update(updateInformation)
            .await()
    }
}