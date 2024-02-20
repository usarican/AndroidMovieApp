package com.example.mymovieapp.features.auth.domain.mapper

import android.net.Uri
import com.example.mymovieapp.features.auth.data.local.entity.UserEntity
import com.example.mymovieapp.features.auth.data.remote.InterestGenreItemResponse
import com.example.mymovieapp.features.auth.data.remote.UserDto
import com.example.mymovieapp.features.auth.data.remote.UserResponse
import com.example.mymovieapp.features.auth.domain.model.AuthUserDataStateModel
import com.example.mymovieapp.utils.Constants.EMPTY_STRING
import com.example.mymovieapp.utils.JsonConverter
import javax.inject.Inject

class UserMapper @Inject constructor(
    private val jsonConverter: JsonConverter,
    private val genreMapper: GenreMapper
) {
    fun responseToEntity(response: UserResponse, userProfilePicture: Uri?): UserEntity =
        with(response) {
            UserEntity(
                userUid = userUid,
                userFullName = userFullName,
                userNickName = userNickName ?: "empty",
                userPhoneNumber = userPhoneNumber,
                userGenre = userGenre,
                userGenreInterestList = jsonConverter.convertObjectToJson(userGenreInterestList),
                userEmail = userEmail,
                userProfilePicture = userProfilePicture.toString()
            )
        }

    fun entityToResponse(entity: UserEntity) : UserResponse =
        with(entity) {
            UserResponse(
                userUid = userUid,
                userFullName = userFullName,
                userNickName = userNickName,
                userGenreInterestList = jsonConverter.convertJsonToObject<List<InterestGenreItemResponse>?>(userGenreInterestList ?: EMPTY_STRING),
                userGenre = userGenre,
                userPhoneNumber = userPhoneNumber,
                userEmail = userEmail,
                userEnteredFirstTime = true
            )
        }

    fun userDtoToUserResponse(userDto: UserDto) : UserResponse {
        return with(userDto){
            UserResponse(
                userUid = userUid ?: EMPTY_STRING,
                userFullName = userFullName,
                userNickName = userNickName,
                userGenreInterestList = userGenreInterestList?.map { genreMapper.interestGenreItemDtoToResponse(it)  },
                userGenre = userGenre,
                userPhoneNumber = userPhoneNumber,
                userEmail = userEmail ?: EMPTY_STRING,
                userEnteredFirstTime = true
            )
        }
    }

    fun authUserDataStateModelToFirebaseUserModel(authUserDataStateModel: AuthUserDataStateModel): Map<String, Any?> {
        val genreDtoList = authUserDataStateModel.userMovieGenreInterestList?.map {
            genreMapper.movieFilterDialogItemToInterestGenreItemDto(it)
        }
        return with(authUserDataStateModel) {
            mapOf(
                "userFullName" to userFullName,
                "userNickName" to userNickName,
                "userPhoneNumber" to userPhoneNumber,
                "userGenre" to userGenre,
                "userGenreInterestList" to genreDtoList,
            )
        }
    }


    fun authUserDataStateModelToUserEntity(authUserDataStateModel: AuthUserDataStateModel): UserEntity {
        val genreDtoList = authUserDataStateModel.userMovieGenreInterestList?.map {
            genreMapper.movieFilterDialogItemToInterestGenreItemDto(it)
        }
        return with(authUserDataStateModel) {
            UserEntity(
                userUid = userUid ?: EMPTY_STRING,
                userFullName = userFullName,
                userNickName = userNickName ?: EMPTY_STRING,
                userPhoneNumber = userPhoneNumber,
                userEmail = userMail ?: EMPTY_STRING,
                userGenreInterestList = jsonConverter.convertObjectToJson(genreDtoList),
                userGenre = userGenre,
                userProfilePicture = userProfilePicture.toString()
            )
        }
    }


}