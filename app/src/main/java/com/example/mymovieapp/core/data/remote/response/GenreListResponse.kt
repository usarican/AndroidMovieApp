package com.example.mymovieapp.core.data.remote.response

data class GenreListResponse(
    val genres : List<GenreDto>
) {
    data class GenreDto(
        val id : Int,
        val name : String
    )
}