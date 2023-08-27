package com.example.mymovieapp.features.home.domain.model
data class Movie(
    val id : Int,
    val title : String,
    val content : String,
    val image : String?,
    val genreList : List<String?>,
    val voteScore : String,
    val releaseYear : String,
    val backImage : String?
)
