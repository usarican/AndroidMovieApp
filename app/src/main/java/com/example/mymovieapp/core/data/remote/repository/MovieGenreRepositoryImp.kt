package com.example.mymovieapp.core.data.remote.repository

import com.example.mymovieapp.core.data.BaseRepository
import com.example.mymovieapp.core.data.State
import com.example.mymovieapp.core.data.local.database.MovieAppDatabase
import com.example.mymovieapp.core.data.remote.datasource.MovieGenreRemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject

open class MovieGenreRepositoryImp @Inject constructor(
    private val movieGenreRemoteDataSource: MovieGenreRemoteDataSource,
    database: MovieAppDatabase
) : MovieGenreRepository, BaseRepository() {

    private val genreDatabase = database.genreDatabase

    fun getMovieGenreListFromDatabase() = apiCall { genreDatabase.getGenreList() }
    override suspend fun getMovieGenreList(language: String): State<Boolean> {
        return withContext(Dispatchers.IO) {
            val localGenreList = genreDatabase.getGenreList()
            if (localGenreList.isEmpty()) {
                val list = async { movieGenreRemoteDataSource.getMovieGenreList(language) }
                genreDatabase.insertGenreList(list.await().genres.map { it.dtoToEntity() })
                State.Success(true)

            } else {
                State.Error(Throwable("Error"))
            }
        }
    }
}
