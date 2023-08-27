package com.example.mymovieapp.features.details.domain.model

data class MovieDetailReview(
    val userName: String,
    val userProfilePicture: String?,
    val userComment: String,
    val userMovieRate : Int?,
    val createdDate : String
)