package com.example.mymovieapp.features.details.data.remote

import com.squareup.moshi.Json

data class MovieDetailCastDto(
    val name : String,
    @Json(name = "character") val characterName : String,
    @Json(name = "profile_path") val profilePicture : String
)
