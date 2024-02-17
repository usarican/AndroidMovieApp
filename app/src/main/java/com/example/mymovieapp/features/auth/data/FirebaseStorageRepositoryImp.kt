package com.example.mymovieapp.features.auth.data

import com.google.firebase.storage.FirebaseStorage
import javax.inject.Inject

class FirebaseStorageRepositoryImp @Inject constructor(
    private val storage: FirebaseStorage
)  : FirebaseStorageRepository{
    override suspend fun uploadUserImageToFirebase() {

    }

    override suspend fun downloadUserImageFromFirebase() {
        TODO("Not yet implemented")
    }
}