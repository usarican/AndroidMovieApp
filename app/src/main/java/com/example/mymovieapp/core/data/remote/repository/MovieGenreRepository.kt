package com.example.mymovieapp.core.data.remote.repository

import com.example.mymovieapp.core.data.State
import kotlinx.coroutines.flow.Flow

interface MovieGenreRepository {
    suspend fun getMovieGenreList(language : String)
}