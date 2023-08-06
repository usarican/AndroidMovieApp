package com.example.mymovieapp.core.data.remote.response

import com.squareup.moshi.Json

data class MovieResponse(
    @Json(name = "page") val page: Int,
    val results: List<MovieDto>,
    @Json(name = "total_results") val totalResults: Int,
    @Json(name = "total_pages") val totalPages: Int
)