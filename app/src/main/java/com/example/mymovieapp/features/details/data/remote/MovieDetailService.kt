package com.example.mymovieapp.features.details.data.remote

import com.example.mymovieapp.core.data.remote.response.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface MovieDetailService {

    @GET("movie/{movie_id}")
    suspend fun getMovieDetail(
        @Path("movie_id") movieId: Int
    ): MovieDetailDto

    @GET("movie/{movie_id}/credits")
    suspend fun getMovieDetailCast(
        @Path("movie_id") movieId: Int
    ): MovieDetailCastResponse

    @GET("movie/{movie_id}/reviews")
    suspend fun getMovieDetailReviews(
        @Path("movie_id") movieId: Int,
        @Path("page") page : Int
    ): MovieDetailReviewResponse

    @GET("movie/{movie_id}/similar")
    suspend fun getMovieDetailMoreLikeThisMovie(
        @Path("movie_id") movieId: Int,
        @Path("page") page : Int
    ): MovieResponse

    @GET("movie/{movie_id}/videos")
    suspend fun getMovieDetailTrailerVideos(
        @Path("movie_id") movieId: Int
    ): MovieDetailVideoResponse

}