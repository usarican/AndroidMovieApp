package com.example.mymovieapp.features.details.data

import androidx.paging.PagingData
import com.example.mymovieapp.core.data.State
import com.example.mymovieapp.core.data.remote.response.MovieDto
import com.example.mymovieapp.features.details.data.remote.*
import kotlinx.coroutines.flow.Flow


interface MovieDetailsRepository {
    fun getMovieDetail(movieId : Int) : Flow<State<MovieDetailDto>>
    suspend fun getMovieDetailForDetailPage(movieId : Int) : MovieDetailDto

    suspend fun getMovieDetailCast(movieId: Int) : MovieDetailCastResponse

    fun getMovieDetailReviews(movieId: Int) : Flow<PagingData<MovieDetailReviewDto>>

    fun getMovieDetailMoreLikeThisMovie(movieId: Int) : Flow<PagingData<MovieDto>>

    suspend fun getMovieDetailTrailerVideos(movieId: Int) : MovieDetailVideoResponse
}