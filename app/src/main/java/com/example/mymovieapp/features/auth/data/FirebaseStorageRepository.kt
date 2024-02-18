package com.example.mymovieapp.features.auth.data

import android.net.Uri
import java.io.File

interface FirebaseStorageRepository  {
    suspend fun uploadUserImageToFirebase(imageUri : Uri,userUid : String)
    suspend fun downloadUserImageFromFirebase()
}