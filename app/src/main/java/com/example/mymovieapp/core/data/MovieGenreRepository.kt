package com.example.mymovieapp.core.data

import com.example.mymovieapp.core.data.remote.response.GenreResponse

interface MovieGenreRepository {
    suspend fun fetchGenreListFromDatabase() : List<GenreResponse>
    suspend fun insertGenreListFromRemoteToDatabase(language : String)
}