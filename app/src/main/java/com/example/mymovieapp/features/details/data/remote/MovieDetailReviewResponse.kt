package com.example.mymovieapp.features.details.data.remote

data class MovieDetailReviewResponse(
    val page : Int,
    val results : List<MovieDetailReviewDto>,
    val total_pages : Int,
    val total_results : Int
)
