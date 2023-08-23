package com.example.mymovieapp.features.details.data.remote

import com.squareup.moshi.Json

data class MovieDetailVideoResponse(
    @Json(name = "results") val videos: List<MovieDetailVideoDto>
)