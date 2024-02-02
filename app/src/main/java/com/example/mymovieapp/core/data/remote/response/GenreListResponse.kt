package com.example.mymovieapp.core.data.remote.response

data class GenreListResponse(
    val genres : List<GenreResponse>
)

data class GenreResponse(
    val id : Int,
    val name : String
)