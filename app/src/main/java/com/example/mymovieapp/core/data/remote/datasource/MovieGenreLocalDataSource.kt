package com.example.mymovieapp.core.data.remote.datasource

import com.example.mymovieapp.core.data.local.database.MovieAppDatabase
import com.example.mymovieapp.core.data.local.entity.GenreEntity
import javax.inject.Inject

class MovieGenreLocalDataSource @Inject constructor(
    database: MovieAppDatabase
) {
    private val genreDatabase = database.genreDatabase
    suspend fun insertGenreListToDatabase(genreList: List<GenreEntity>) =
        genreDatabase.insertGenreList(genreList)

    suspend fun fetchGenreListFromDatabase(): List<GenreEntity> = genreDatabase.getGenreList()
}