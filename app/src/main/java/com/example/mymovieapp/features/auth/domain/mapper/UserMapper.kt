package com.example.mymovieapp.features.auth.domain.mapper

import com.example.mymovieapp.features.auth.data.local.entity.UserEntity
import com.example.mymovieapp.features.auth.data.remote.UserResponse
import com.example.mymovieapp.utils.JsonConverter
import javax.inject.Inject

class UserMapper @Inject constructor(
    private val jsonConverter: JsonConverter
) {
    fun responseToEntity(response : UserResponse) : UserEntity =
        with(response) {
            UserEntity(
                userUid = userUid,
                userFullName = userFullName,
                userNickName = userNickName ?: "empty",
                userPhoneNumber = userPhoneNumber,
                userGenre = userGenre,
                userGenreInterestList = jsonConverter.convertObjectToJson(userGenreInterestList),
                userEmail = userEmail,
                userEnteredFirstTime = userEnteredFirstTime

            )
        }

}