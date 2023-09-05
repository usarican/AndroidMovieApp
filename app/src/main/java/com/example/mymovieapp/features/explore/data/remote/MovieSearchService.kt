package com.example.mymovieapp.features.explore.data.remote

import com.example.mymovieapp.core.data.remote.response.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieSearchService {

    @GET("search/movie")
    suspend fun getSearchingResult(
        @Query("query") searchString : String,
        @Query("page") page : Int
    ): MovieResponse

    @GET("discover/movie")
    suspend fun getDiscoveryMovieResult(
        @Query("page") page : Int,
        @Query("language") language : String,
        @Query("sort_by") sortFilter : String?,
        @Query("with_genres") genreListFilter : String?,
        @Query("region") regionFilter : String?,
        @Query("year") yearFilter : Int?,
    ): MovieResponse
}