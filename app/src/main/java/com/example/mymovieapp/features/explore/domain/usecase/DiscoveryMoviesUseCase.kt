package com.example.mymovieapp.features.explore.domain.usecase

import androidx.paging.PagingData
import androidx.paging.map
import com.example.mymovieapp.core.data.State
import com.example.mymovieapp.features.explore.data.MovieExploreRepository
import com.example.mymovieapp.features.explore.ui.dialog.MovieFilterItem
import com.example.mymovieapp.features.explore.ui.dialog.MovieFilterUtils
import com.example.mymovieapp.features.home.data.HomeRepository
import com.example.mymovieapp.features.home.domain.mapper.MovieMapper
import com.example.mymovieapp.features.home.domain.model.Movie
import com.example.mymovieapp.utils.extensions.separateIntValueWithCommaAndReturnString
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DiscoveryMoviesUseCase @Inject constructor(
    private val movieExploreRepository: MovieExploreRepository,
    private val movieMapper: MovieMapper,
    private val movieFilterUtils: MovieFilterUtils
) {
    operator fun invoke(
        language : String,
        movieFilterItem: MovieFilterItem
    ) : Flow<PagingData<Movie>> {
        return movieExploreRepository.getDiscoveryMovieResults(
            language = language,
            sortFilter = movieFilterItem.sortFilterItem?.itemCode as? String?,
            genreListFilter = movieFilterItem.genresFilterItem.map { it.itemCode as Int? }.separateIntValueWithCommaAndReturnString(),
            regionFilter = movieFilterItem.regionFilterItem.itemCode as String?,
            yearFilter = movieFilterItem.timeFilterItem.itemCode as Int?
        ).map { pagingData ->
            pagingData.map {
                movieMapper.mapOnMovieDto(it)
            }
        }
    }
}