package com.example.mymovieapp.features.details.data.remote

import retrofit2.http.GET
import retrofit2.http.Path

interface MovieDetailService {

    @GET("movie/{movie_id}")
    suspend fun getMovieDetail(
        @Path("movie_id") movieId : Int
    ) : MovieDetailDto
}