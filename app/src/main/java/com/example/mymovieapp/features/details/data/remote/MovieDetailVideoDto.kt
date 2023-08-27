package com.example.mymovieapp.features.details.data.remote

import com.squareup.moshi.Json

data class MovieDetailVideoDto(
    val type : String,
    val name : String,
    @Json(name = "published_at") val publishTime : String,
    val key : String
)
