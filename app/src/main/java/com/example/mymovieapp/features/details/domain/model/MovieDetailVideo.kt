package com.example.mymovieapp.features.details.domain.model

data class MovieDetailVideo(
    val type : String,
    val name : String,
    val date : String,
    val key : String
) {
    enum class VideoType(val typeName : String) {
        TRAILER("Trailer"),
        TEASER("Teaser");
    }
}
