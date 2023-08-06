package com.example.mymovieapp.core.data.remote

import com.example.mymovieapp.core.data.remote.response.GenreListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieGenreService {

    @GET("genre/movie/list")
    suspend fun getMovieGenreList(
        @Query("language") language: String
    ): GenreListResponse
}