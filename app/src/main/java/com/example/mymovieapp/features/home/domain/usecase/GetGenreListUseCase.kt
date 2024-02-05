package com.example.mymovieapp.features.home.domain.usecase

import com.example.mymovieapp.core.data.State
import com.example.mymovieapp.core.data.MovieGenreRepositoryImp
import com.example.mymovieapp.core.data.remote.response.GenreResponse
import com.example.mymovieapp.features.home.data.HomeRepository
import com.example.mymovieapp.features.home.domain.mapper.GenreListMapper
import com.example.mymovieapp.utils.extensions.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetGenreListUseCase @Inject constructor(
    private val genreRepository : MovieGenreRepositoryImp
) {
    operator fun invoke(language: String): Flow<State<List<GenreResponse>>> {
        return flow {
            emit(State.Loading)
            try {
                val list = genreRepository.fetchGenreListFromDatabase()
                emit(State.Success(list))
            }catch (e : Exception) {
                emit(State.Error(e))
            }
        }
    }

}