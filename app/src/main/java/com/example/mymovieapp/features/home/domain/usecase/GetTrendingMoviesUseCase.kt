package com.example.mymovieapp.features.home.domain.usecase

import com.example.mymovieapp.core.data.State
import com.example.mymovieapp.core.data.remote.response.GenreListDto
import com.example.mymovieapp.core.data.remote.response.MovieResponse
import com.example.mymovieapp.features.home.data.HomeRepository
import com.example.mymovieapp.features.home.domain.mapper.GenreListMapper
import com.example.mymovieapp.features.home.domain.mapper.MovieMapper
import com.example.mymovieapp.features.home.domain.model.Movie
import com.example.mymovieapp.utils.extensions.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetTrendingMoviesUseCase @Inject constructor(
    private val homeRepository: HomeRepository,
    private val genreListMapper: GenreListMapper,
    private val movieMapper: MovieMapper,
    private val genreListUseCase: GetGenreListUseCase
){
    operator fun invoke(language : String) : Flow<State<List<Movie>>> {
        return combine(
            homeRepository.getTrendingMoviesOfWeek(language),
            genreListUseCase(language)
        ) { stateMovieResponse : State<MovieResponse>, genreList: Map<Int, String> ->
            stateMovieResponse.map { movieResponse ->
                movieResponse.results.map { movieDto ->
                    val movie = movieMapper.mapOnMovieDto(movieDto)
                    movie.copy(
                        genreList = genreListMapper.mapOnGenreListKeyToValue(
                            genreListMap = genreList,
                            genreKeys = movieDto.genreIds
                        )
                    )
                }
            }
        }

    }

}