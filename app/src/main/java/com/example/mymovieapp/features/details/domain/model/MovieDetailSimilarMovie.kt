package com.example.mymovieapp.features.details.domain.model

data class MovieDetailSimilarMovie(
    val id : Int,
    val title : String,
    val content : String,
    val image : String?,
    val genreList : String?,
    val movieScore : Int?,
    val releaseYear : String,
    val backImage : String?
)