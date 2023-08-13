package com.example.mymovieapp.features.details.data.remote

import com.example.mymovieapp.core.data.remote.response.GenreListResponse
import com.squareup.moshi.Json

data class MovieDetailDto(
    val id : Int,
    @Json(name = "overview") val content : String,
    @Json(name = "runtime") val movieTime : Int,
    @Json(name = "poster_path") val posterPath: String?,
    @Json(name = "release_date") val releaseDate: String?,
    val genres: List<GenreListResponse.GenreDto>,
    @Json(name = "vote_count") val voteCount: Int,
    @Json(name = "vote_average") val voteAverage: Double,
    @Json(name = "status") val status : String
)