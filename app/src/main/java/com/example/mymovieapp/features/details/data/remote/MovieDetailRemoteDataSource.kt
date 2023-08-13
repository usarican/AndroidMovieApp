package com.example.mymovieapp.features.details.data.remote

import javax.inject.Inject

class MovieDetailRemoteDataSource @Inject constructor(
    private val movieDetailService : MovieDetailService
) {
    suspend fun getMovieDetails(movieId : Int) : MovieDetailDto {
        return movieDetailService.getMovieDetail(movieId)
    }
}