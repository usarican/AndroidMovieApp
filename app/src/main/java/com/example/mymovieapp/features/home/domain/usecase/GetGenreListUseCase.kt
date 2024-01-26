package com.example.mymovieapp.features.home.domain.usecase

import com.example.mymovieapp.core.data.State
import com.example.mymovieapp.core.data.remote.repository.MovieGenreRepositoryImp
import com.example.mymovieapp.features.home.data.HomeRepository
import com.example.mymovieapp.features.home.domain.mapper.GenreListMapper
import com.example.mymovieapp.utils.extensions.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetGenreListUseCase @Inject constructor(
    private val homeRepository: HomeRepository,
    private val genreListMapper: GenreListMapper,
    private val genreRepository : MovieGenreRepositoryImp
) {
    operator fun invoke(language: String): Flow<State<Map<Int, String>>> {
        return homeRepository.getMovieGenreList(language).map { state ->
            state.map { genreListResponse ->

                genreListMapper.mapOnGenresToMap(genreListResponse)
            }
        }
    }

    suspend fun insertGenreListInDb(language : String) = genreRepository.getMovieGenreList(language)

    fun getGenreListFromDatabase() = genreRepository.getMovieGenreListFromDatabase().map { state ->
        state.map { list ->
            list.map { it.entityToDto() }
        }
    }

}