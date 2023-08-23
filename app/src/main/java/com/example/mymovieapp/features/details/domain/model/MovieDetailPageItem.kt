package com.example.mymovieapp.features.details.domain.model

import androidx.paging.PagingData
import com.example.mymovieapp.features.details.data.remote.MovieDetailDto
import com.example.mymovieapp.features.home.domain.model.Movie

data class MovieDetailPageItem(
    val id: Int,
    val movieTitle: String,
    val content: String,
    val movieTime: String,
    val posterImage: String?,
    val backgroundImage : String?,
    val releaseDate: String?,
    val genres: List<String>,
    val voteCount: String,
    val movieScore: String,
    val status: String,
    val productionCountry : MovieDetailDto.ProductionCountryDto? = null,
    val movieReviews : PagingData<MovieDetailReview>? = null,
    val movieCasts : List<MovieDetailCast>? = null,
    val movieMoreLikeThis : PagingData<MovieDetailSimilarMovie>? = null,
    val movieTrailers : List<MovieDetailVideo>? = null
)