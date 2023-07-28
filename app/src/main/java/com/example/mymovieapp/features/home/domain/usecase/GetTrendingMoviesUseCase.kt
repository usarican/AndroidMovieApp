package com.example.mymovieapp.features.home.domain.usecase

import com.example.mymovieapp.core.data.State
import com.example.mymovieapp.core.data.remote.response.MovieResponse
import com.example.mymovieapp.features.home.data.HomeRepository
import com.example.mymovieapp.features.home.domain.mapper.GenreListMapper
import com.example.mymovieapp.features.home.domain.mapper.MovieMapper
import com.example.mymovieapp.features.home.domain.model.Movie
import com.example.mymovieapp.utils.extensions.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
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
            ) { stateMovieResponse : State<MovieResponse>, stateGenreList : State<Map<Int, String>> ->
                State.Loading
                val genreList = if (stateGenreList is State.Success) stateGenreList.data else emptyMap()
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
            }.catch {
                State.Error(it)
            }
        }

    }
