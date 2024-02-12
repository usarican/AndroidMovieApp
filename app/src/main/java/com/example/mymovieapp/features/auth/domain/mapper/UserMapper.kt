package com.example.mymovieapp.features.auth.domain.mapper

import com.example.mymovieapp.features.auth.domain.model.User
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class UserMapper @Inject constructor() {
    fun firebaseUserToUser(firebaseUser: FirebaseUser) : User =
        with(firebaseUser) {
            User(
                userUID = uid,
                userEmail = email ?: ""
            )
        }
}