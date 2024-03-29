package com.example.mymovieapp.features.details.data.remote

import com.example.mymovieapp.core.data.remote.response.GenreListResponse
import com.example.mymovieapp.core.data.remote.response.GenreResponse
import com.squareup.moshi.Json

data class MovieDetailDto(
    val id : Int,
    val title : String,
    @Json(name = "overview") val content : String,
    @Json(name = "runtime") val movieTime : Int,
    @Json(name = "poster_path") val posterPath: String?,
    @Json(name = "backdrop_path") val backdropPath : String?,
    @Json(name = "release_date") val releaseDate: String?,
    @Json(name = "production_countries") val productionCountries : List<ProductionCountryDto>,
    val genres: List<GenreResponse>,
    @Json(name = "vote_count") val voteCount: Int,
    @Json(name = "vote_average") val voteAverage: Double,
    @Json(name = "status") val status : String
) {
    data class ProductionCountryDto(
        val name : String
    )
}