package com.example.mymovieapp.features.auth.data.remote

class UserDto private constructor(
    val userUid : String?,
    val userFullName : String?,
    val userNickName : String?,
    val userPhoneNumber : String?,
    val userGenre : String?,
    val userGenreInterestList : List<InterestGenreItemDto>?,
    val userEmail : String?,
    val userEnteredFirstTime : Boolean?
) {
    class Builder {
        private var userUid : String? = null
        private var userFullName : String? = null
        private var userNickName : String? = null
        private var userPhoneNumber : String? = null
        private var userGenre : String? = null
        private var userGenreInterestList : List<InterestGenreItemDto>? = null
        private var userEmail : String? = null
        private var userEnteredFirstTime : Boolean? = true

        fun setUserUid(uid : String) = apply { this.userUid = uid }
        fun setUserFullName(userFullName : String) = apply { this.userFullName = userFullName }
        fun setUserNickName(userNickName : String) = apply { this.userNickName = userNickName }
        fun setUserPhoneNumber(userPhoneNumber : String) = apply { this.userPhoneNumber = userPhoneNumber }
        fun setUserGenre(userGenre : String) = apply { this.userGenre = userGenre }
        fun setUserGenreInterestList(userInterestGenreList : List<InterestGenreItemDto>) = apply { this.userGenreInterestList = userInterestGenreList }
        fun setUserEmail(userEmail : String) = apply { this.userEmail = userEmail }
        fun setUserEnteredFirstTime(isUserEnteredFirstTime : Boolean) = apply { this.userEnteredFirstTime = isUserEnteredFirstTime }

        fun build() = UserDto(
            userUid = userUid,
            userFullName = userFullName,
            userNickName = userNickName,
            userPhoneNumber = userPhoneNumber,
            userGenre = userGenre,
            userGenreInterestList = userGenreInterestList,
            userEmail = userEmail,
            userEnteredFirstTime = userEnteredFirstTime
        )
    }
}

data class InterestGenreItemDto(
    val genreId : Int?,
    val genreName : String?
)
