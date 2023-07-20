package com.example.mymovieapp.features.home.data

import androidx.paging.PagingData
import com.example.mymovieapp.core.data.State
import com.example.mymovieapp.core.data.remote.response.GenreListResponse
import com.example.mymovieapp.core.data.remote.response.MovieDto
import com.example.mymovieapp.core.data.remote.response.MovieResponse
import kotlinx.coroutines.flow.Flow


interface HomeRepository {

    fun getTrendingMoviesOfWeek(language : String) : Flow<State<MovieResponse>>

    suspend fun getPopularMovies(page : Int, language : String) : Flow<PagingData<MovieDto>>

    suspend fun getTopRatedMovies(page : Int, language : String) : Flow<PagingData<MovieDto>>

    suspend fun getUpComingMovies(page : Int, language : String) : Flow<PagingData<MovieDto>>

    fun getMovieGenreList(language : String) : Flow<GenreListResponse>
}