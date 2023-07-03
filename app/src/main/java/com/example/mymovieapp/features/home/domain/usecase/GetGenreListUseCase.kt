package com.example.mymovieapp.features.home.domain.usecase

import com.example.mymovieapp.core.data.State
import com.example.mymovieapp.features.home.data.HomeRepository
import com.example.mymovieapp.features.home.domain.mapper.GenreListMapper
import com.example.mymovieapp.utils.extensions.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetGenreListUseCase @Inject constructor(
    private val homeRepository: HomeRepository,
    private val genreListMapper: GenreListMapper
) {

    suspend operator fun invoke(language: String): Flow<Map<Int, String>> {
        return homeRepository.getMovieGenreList(language).map { genreListDto ->
            genreListMapper.mapOnGenresToMap(genreListDto)
        }
    }
}