package com.example.mymovieapp.features.details.data.remote

import com.squareup.moshi.Json

data class MovieDetailCastResponse(
    val id : Int,
    @Json(name = "cast") val movieCast : List<MovieDetailCastDto>
)
