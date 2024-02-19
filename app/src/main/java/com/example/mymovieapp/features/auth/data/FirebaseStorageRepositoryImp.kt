package com.example.mymovieapp.features.auth.data

import android.net.Uri
import com.google.firebase.storage.FileDownloadTask
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageException
import kotlinx.coroutines.tasks.await
import java.io.File
import javax.inject.Inject

class FirebaseStorageRepositoryImp @Inject constructor(
    private val storage: FirebaseStorage
)  : FirebaseStorageRepository{

    private val storageRef = storage.reference
    override suspend fun uploadUserImageToFirebase(imageUri : Uri,userUid : String) {
        val userProfilePictureRef = storageRef.child("users/userImages/$userUid/profilePicture.jpg")
        userProfilePictureRef.putFile(imageUri).await()
    }

    override suspend fun downloadUserImageFromFirebase(userUid : String) : Uri? =
        try {
            storageRef.child("users/userImages/$userUid/profilePicture.jpg").downloadUrl.await()
        }catch (exception : StorageException){
            if (exception.errorCode == StorageException.ERROR_OBJECT_NOT_FOUND){
                null
            } else {
                throw exception
            }
        }



}