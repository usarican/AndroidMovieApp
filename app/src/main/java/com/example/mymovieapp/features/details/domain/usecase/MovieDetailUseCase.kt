package com.example.mymovieapp.features.details.domain.usecase

import com.example.mymovieapp.core.data.State
import com.example.mymovieapp.features.details.data.MovieDetailsRepository
import com.example.mymovieapp.features.details.domain.mapper.DetailsGenreListMapper
import com.example.mymovieapp.features.details.domain.mapper.MovieDetailMapper
import com.example.mymovieapp.features.details.domain.model.MovieDetail
import com.example.mymovieapp.features.home.domain.mapper.GenreListMapper
import com.example.mymovieapp.utils.extensions.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

import javax.inject.Inject

class MovieDetailUseCase @Inject constructor(
    private val movieDetailsRepository: MovieDetailsRepository,
    private val movieDetailMapper: MovieDetailMapper,
    private val genreListMapper: DetailsGenreListMapper
) {

    fun getMovieDetail(movieId : Int) : Flow<State<MovieDetail>> {
        return movieDetailsRepository.getMovieDetail(movieId).map { state ->
            state.map { movieDetailDto ->
                movieDetailMapper.mapOnMovieDetailDtoToMovieDetail(movieDetailDto).copy(
                    genres = genreListMapper.mapToGenreDtoToGenreList(movieDetailDto.genres)
                )
            }
        }
    }
}