package com.example.mymovieapp.features.auth.data

interface FirebaseStorageRepository  {
    suspend fun uploadUserImageToFirebase()
    suspend fun downloadUserImageFromFirebase()
}