package com.example.mymovieapp.core.data.remote.repository

import com.example.mymovieapp.core.data.BaseRepository
import com.example.mymovieapp.core.data.State
import com.example.mymovieapp.core.data.local.database.MovieAppDatabase
import com.example.mymovieapp.core.data.remote.datasource.MovieGenreRemoteDataSource
import com.example.mymovieapp.core.di.IoDispatcher
import com.example.mymovieapp.utils.extensions.converter
import com.example.mymovieapp.utils.extensions.map
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

open class MovieGenreRepositoryImp @Inject constructor(
    private val movieGenreRemoteDataSource: MovieGenreRemoteDataSource,
    private val database: MovieAppDatabase,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : MovieGenreRepository, BaseRepository() {

    private val genreDatabase = database.genreDatabase
    override suspend fun getMovieGenreList(language: String){
        return withContext(dispatcher) {
            val localGenreList = genreDatabase.getGenreList()
            if (localGenreList.isEmpty()) {
                Timber.tag("Utku").d("Database is Empty")
                apiCall {  movieGenreRemoteDataSource.getMovieGenreList(language) }.collectLatest { state ->
                   state.converter(
                        doWhenStateSuccess = { response ->
                            Timber.tag("Utku").d("Response is success")
                            genreDatabase.insertGenreList(response.genres.map { it.dtoToEntity() })
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
