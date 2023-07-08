package com.example.mymovieapp.features.home.domain.model





data class Movie(
    val id : Int,
    val title : String,
    val content : String,
    val image : String?,
    val genreList : List<String?>,
    val voteScore : Double
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val movie : Movie = other as Movie
        return id == movie.id
    }
}
