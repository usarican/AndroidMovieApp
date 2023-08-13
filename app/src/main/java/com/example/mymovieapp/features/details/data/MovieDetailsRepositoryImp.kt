package com.example.mymovieapp.features.details.data

import com.example.mymovieapp.core.data.BaseRepository
import com.example.mymovieapp.core.data.State
import com.example.mymovieapp.features.details.data.remote.MovieDetailDto
import com.example.mymovieapp.features.details.data.remote.MovieDetailRemoteDataSource

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MovieDetailsRepositoryImp @Inject constructor(
    private val movieDetailRemoteDataSource: MovieDetailRemoteDataSource
) : BaseRepository(),MovieDetailsRepository{


    override fun getMovieDetail(movieId: Int): Flow<State<MovieDetailDto>> {
        return apiCall { movieDetailRemoteDataSource.getMovieDetails(movieId) }
    }

}