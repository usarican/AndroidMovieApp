package com.example.mymovieapp.features.auth.data

import android.net.Uri
import com.google.firebase.storage.FileDownloadTask
import java.io.File

interface FirebaseStorageRepository  {
    suspend fun uploadUserImageToFirebase(imageUri : Uri,userUid : String)
    suspend fun downloadUserImageFromFirebase(userUid: String) : Uri?
}