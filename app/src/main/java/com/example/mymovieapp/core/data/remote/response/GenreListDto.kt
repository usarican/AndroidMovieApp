package com.example.mymovieapp.core.data.remote.response

data class GenreListDto(
    val genres : List<Genre>
) {
    data class Genre(
        val id : Int,
        val name : String
    )
}