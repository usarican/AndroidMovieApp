package com.example.mymovieapp.core.data.remote.datasource

import com.example.mymovieapp.core.data.remote.MovieGenreService
import javax.inject.Inject

class MovieGenreRemoteDataSource @Inject constructor(
    private val genreService: MovieGenreService
) {
    suspend fun getMovieGenreList(language : String) = genreService.getMovieGenreList(language)
}