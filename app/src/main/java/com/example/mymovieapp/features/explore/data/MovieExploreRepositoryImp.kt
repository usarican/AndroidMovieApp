package com.example.mymovieapp.features.explore.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.mymovieapp.core.data.remote.response.MovieDto
import com.example.mymovieapp.features.explore.data.remote.MovieExploreDataSource
import com.example.mymovieapp.utils.Constants
import com.example.mymovieapp.utils.MoviesPagingSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MovieExploreRepositoryImp @Inject constructor(
    private val movieExploreDataSource: MovieExploreDataSource
) : MovieExploreRepository{

    override fun getSearchingMovieResults(searchingQuery: String): Flow<PagingData<MovieDto>> {
        return Pager(
            config = PagingConfig(
                pageSize = Constants.ITEMS_PER_PAGE
            ),
            pagingSourceFactory = {
                MoviesPagingSource(
                    getMoviesFunc = { page ->
                        movieExploreDataSource.getSearchingMovieResult(
                            searchingQuery = searchingQuery,
                            page = page
                        )
                    }
                )
            }
        ).flow
    }

    override fun getDiscoveryMovieResults(
        language: String,
        sortFilter: String?,
        genreListFilter: String?,
        regionFilter: String?,
        yearFilter: Int?
    ): Flow<PagingData<MovieDto>> {
        return Pager(
            config = PagingConfig(
                pageSize = Constants.ITEMS_PER_PAGE
            ),
            pagingSourceFactory = {
                MoviesPagingSource(
                    getMoviesFunc = { page ->
                        movieExploreDataSource.getDiscoveryMovieResult(
                            page = page,
                            language = language,
                            sortFilter = sortFilter,
                            genreListFilter = genreListFilter,
                            regionFilter = regionFilter,
                            yearFilter = yearFilter
                        )
                    }
                )
            }
        ).flow
    }
}