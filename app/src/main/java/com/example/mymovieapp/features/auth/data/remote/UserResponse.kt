package com.example.mymovieapp.features.auth.data.remote

data class UserResponse(
    val userUid: String,
    val userFullName: String?,
    val userNickName: String?,
    val userPhoneNumber: String?,
    val userGenre: String?,
    val userGenreInterestList: List<InterestGenreItemResponse>?,
    val userEmail: String,
    val userEnteredFirstTime: Boolean
) {
    constructor() : this("", null, "", null, null, null, "", true)
}

data class InterestGenreItemResponse(
    val genreId : Int?,
    val genreName : String?
) {
    constructor() : this(null,null)
}