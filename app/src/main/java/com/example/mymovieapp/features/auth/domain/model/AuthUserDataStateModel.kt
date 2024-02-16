package com.example.mymovieapp.features.auth.domain.model

import android.graphics.Bitmap
import com.example.mymovieapp.features.explore.ui.dialog.MovieFilterDialogItem

data class AuthUserDataStateModel(
    var userUid : String? = null,
    var userMail : String? = null,
    var userProfilePicture : Bitmap? = null,
    var userFullName : String? = null,
    var userNickName : String? = null,
    var userPhoneNumber : String? = null,
    var userGenre : String? = null,
    var userMovieGenreInterestList : List<MovieFilterDialogItem>? = null
)
