package com.example.mymovieapp.core.data

import com.example.mymovieapp.core.data.remote.datasource.MovieGenreLocalDataSource
import com.example.mymovieapp.core.data.remote.datasource.MovieGenreRemoteDataSource
import com.example.mymovieapp.core.data.remote.response.GenreResponse
import com.example.mymovieapp.core.di.IoDispatcher
import com.example.mymovieapp.utils.extensions.converter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MovieGenreRepositoryImp @Inject constructor(
    private val movieGenreRemoteDataSource: MovieGenreRemoteDataSource,
    private val movieGenreLocalDataSource: MovieGenreLocalDataSource,
    private val mapper: MovieGenreMapper,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : MovieGenreRepository, BaseRepository() {


    override suspend fun fetchGenreListFromDatabase(): List<GenreResponse> =
        movieGenreLocalDataSource.fetchGenreListFromDatabase().map { mapper.mapToResponse(it) }

    override suspend fun insertGenreListFromRemoteToDatabase(language: String) {
        withContext(dispatcher) {
            val localGenreList = fetchGenreListFromDatabase()
            if (localGenreList.isEmpty()) {
                apiCall { movieGenreRemoteDataSource.getMovieGenreList(language) }
                    .collectLatest { state ->
                        state.converter(
                            doWhenStateSuccess = { response ->
                                movieGenreLocalDataSource.insertGenreListToDatabase(
                                    response.genres.map { mapper.mapToEntity(it) }
                                )
                            },
                            doWhenStateError = {
                                throw Exception(it.message)
                            }
                        )
                    }
            }
        }
    }
}
