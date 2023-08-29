package com.example.mymovieapp.features.explore.domain.usecase

import androidx.paging.PagingData
import androidx.paging.map
import com.example.mymovieapp.core.data.remote.response.MovieDto
import com.example.mymovieapp.features.home.data.HomeRepository
import com.example.mymovieapp.features.home.data.remote.HomeRemoteDataSource
import com.example.mymovieapp.features.home.domain.mapper.MovieMapper
import com.example.mymovieapp.features.home.domain.model.CategoryType
import com.example.mymovieapp.features.home.domain.model.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SeeAllMoviesUseCase @Inject constructor(
    private val homeRepository: HomeRepository,
    private val movieMapper: MovieMapper
) {
    operator fun invoke(categoryType: CategoryType,language : String) : Flow<PagingData<Movie>> {

        val pagingData : Flow<PagingData<MovieDto>> = when(categoryType){
            CategoryType.POPULAR ->  homeRepository.getPopularMovies(language)
            CategoryType.TOP_RATED -> homeRepository.getTopRatedMovies(language)
            CategoryType.UP_COMING -> homeRepository.getUpComingMovies(language)
        }

        return pagingData.map { value : PagingData<MovieDto> ->
            value.map {
                movieMapper.mapOnMovieDto(it)
            }
        }
    }
}