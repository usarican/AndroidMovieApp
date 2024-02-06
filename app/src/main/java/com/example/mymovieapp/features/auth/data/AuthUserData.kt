package com.example.mymovieapp.features.auth.data

import android.graphics.Bitmap
import com.example.mymovieapp.features.explore.ui.dialog.MovieFilterDialogItem

data class AuthUserData(
    var userMail : String? = null,
    var userProfilePicture : Bitmap? = null,
    var userFullName : String? = null,
    var userNickName : String? = null,
    var userPhoneNumber : String? = null,
    var userGenre : String? = null,
    var userMovieGenreInterestList : List<MovieFilterDialogItem>? = null
)
