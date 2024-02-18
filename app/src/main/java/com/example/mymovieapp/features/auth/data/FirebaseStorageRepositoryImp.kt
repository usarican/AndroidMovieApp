package com.example.mymovieapp.features.auth.data

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.io.File
import javax.inject.Inject

class FirebaseStorageRepositoryImp @Inject constructor(
    private val storage: FirebaseStorage
)  : FirebaseStorageRepository{

    private val storageRef = storage.reference
    override suspend fun uploadUserImageToFirebase(imageUri : Uri,userUid : String) {
        val userProfilePictureRef = storageRef.child("users/userImages/$userUid")
        userProfilePictureRef.putFile(imageUri).await()
    }

    override suspend fun downloadUserImageFromFirebase() {
        TODO("Not yet implemented")
    }
}