package com.example.mymovieapp.features.details.data.remote

import com.example.mymovieapp.core.data.remote.response.MovieResponse
import javax.inject.Inject

class MovieDetailRemoteDataSource @Inject constructor(
    private val movieDetailService : MovieDetailService
) {
    suspend fun getMovieDetails(movieId : Int) : MovieDetailDto {
        return movieDetailService.getMovieDetail(movieId)
    }
    suspend fun getMovieDetailCast(movieId : Int) : MovieDetailCastResponse =
        movieDetailService.getMovieDetailCast(movieId)

    suspend fun getMovieDetailReviews(movieId : Int, page : Int) : MovieDetailReviewResponse =
        movieDetailService.getMovieDetailReviews(movieId, page)

    suspend fun getMovieDetailMoreLikeThisMovie(movieId : Int, page : Int) : MovieResponse =
        movieDetailService.getMovieDetailMoreLikeThisMovie(movieId,page)

    suspend fun getMovieDetailTrailerVideos(movieId : Int) : MovieDetailVideoResponse =
        movieDetailService.getMovieDetailTrailerVideos(movieId)
}