package com.example.mymovieapp.features.details.data

import com.example.mymovieapp.core.data.State
import com.example.mymovieapp.features.details.data.remote.MovieDetailDto
import kotlinx.coroutines.flow.Flow


interface MovieDetailsRepository {
    fun getMovieDetail(movieId : Int) : Flow<State<MovieDetailDto>>
}