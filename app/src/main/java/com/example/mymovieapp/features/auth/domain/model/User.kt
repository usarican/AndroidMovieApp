package com.example.mymovieapp.features.auth.domain.model

data class User(
    val userUID : String,
    val userEmail : String,
    val userFirstTimeEnter : Boolean = true,
)
