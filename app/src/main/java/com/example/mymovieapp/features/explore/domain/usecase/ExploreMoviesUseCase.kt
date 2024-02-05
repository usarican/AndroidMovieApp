package com.example.mymovieapp.features.explore.domain.usecase

import androidx.paging.PagingData
import androidx.paging.map
import com.example.mymovieapp.core.data.State
import com.example.mymovieapp.core.data.remote.response.GenreResponse
import com.example.mymovieapp.core.data.remote.response.MovieDto
import com.example.mymovieapp.features.explore.data.MovieExploreRepository
import com.example.mymovieapp.features.home.domain.mapper.GenreListMapper
import com.example.mymovieapp.features.home.domain.mapper.MovieMapper
import com.example.mymovieapp.features.home.domain.model.Movie
import com.example.mymovieapp.features.home.domain.usecase.GetGenreListUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class ExploreMoviesUseCase @Inject constructor(
    private val movieExploreRepository: MovieExploreRepository,
    private val movieMapper: MovieMapper,
    private val genreListUseCase: GetGenreListUseCase,
    private val genreListMapper: GenreListMapper,
) {

    fun getSearchingMovieResults(searchingQuery : String?,language : String) : Flow<PagingData<Movie>?> {
        if (searchingQuery == null) {
            return flow {
                emit(null)
            }
        } else {
            return genreListUseCase.invoke(language).combine(
                movieExploreRepository.getSearchingMovieResults(searchingQuery)
            ) { genreListMapState : State<List<GenreResponse>>, searchingMoviePagingData : PagingData<MovieDto> ->
                if (genreListMapState is State.Success) {
                    val genreListMap = genreListMapState.data
                    searchingMoviePagingData.map { movieDto ->
                        val movie = movieMapper.mapOnMovieDto(movieDto)
                        movie.copy(
                            genreList = genreListMapper.mapOnGenreListKeyToValue(genreListMap, movieDto.genreIds)
                        )
                    }
                }
                else null
            }
        }
    }
}