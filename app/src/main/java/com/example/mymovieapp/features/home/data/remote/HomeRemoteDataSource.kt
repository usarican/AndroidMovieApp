package com.example.mymovieapp.features.home.data.remote

import com.example.mymovieapp.core.data.remote.MovieGenreService
import com.example.mymovieapp.core.data.remote.response.GenreListResponse
import com.example.mymovieapp.core.data.remote.response.MovieResponse
import javax.inject.Inject

class HomeRemoteDataSource @Inject constructor(
    private val homeService: HomeService,
    private val movieGenreService: MovieGenreService
) {

    suspend fun getTrendingMoviesOfWeek(language : String) : MovieResponse {
        return homeService.getTrendingMoviesOfTheWeek(language = language)
    }

    suspend fun getPopularMovies(page : Int, language : String) : MovieResponse {
        return homeService.getPopularMovies(
            page = page,
            language = language
        )
    }

    suspend fun getTopRatedMovies(page : Int, language : String) : MovieResponse {
        return homeService.getTopRatedMovies(
            page = page,
            language = language
        )
    }

    suspend fun getUpComingMovies(page : Int, language : String) : MovieResponse {
        return homeService.getUpComingMovies(
            page = page,
            language = language
        )
    }

    suspend fun getMovieGenreList(language : String) : GenreListResponse {
        return movieGenreService.getMovieGenreList(language)
    }
}