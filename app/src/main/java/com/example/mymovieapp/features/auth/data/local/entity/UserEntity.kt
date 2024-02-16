package com.example.mymovieapp.features.auth.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val userUid : String,
    val userFullName : String?,
    val userNickName : String,
    val userPhoneNumber : String?,
    val userGenre : String?,
    val userGenreInterestList : String?,
    val userEmail : String,
    val userEnteredFirstTime : Boolean
)
