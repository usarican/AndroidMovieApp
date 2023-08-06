package com.example.mymovieapp.features.home.data.remote

import com.example.mymovieapp.core.data.remote.response.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface HomeService {

    @GET("trending/movie/week")
    suspend fun getTrendingMoviesOfTheWeek(
        @Query("page") page : Int = 1,
        @Query("language") language : String = "en"
    ) : MovieResponse

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("page") page : Int,
        @Query("language") language : String = "en"
    ) : MovieResponse

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("page") page : Int,
        @Query("language") language : String = "en"
    ) : MovieResponse

    @GET("movie/upcoming")
    suspend fun getUpComingMovies(
        @Query("page") page : Int,
        @Query("language") language : String = "en"
    ) : MovieResponse
}