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
}