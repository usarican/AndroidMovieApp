package com.example.mymovieapp.utils

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.mymovieapp.features.details.data.remote.MovieDetailRemoteDataSource
import com.example.mymovieapp.features.details.data.remote.MovieDetailReviewDto
import timber.log.Timber

class MovieDetailReviewPagingSource (
    private val movieDetailRemoteDataSource: MovieDetailRemoteDataSource,
    private val movieId : Int
) : PagingSource<Int,MovieDetailReviewDto>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieDetailReviewDto> {
        val nextPage = params.key ?: Constants.STARTING_PAGE
        return try {
            val response = movieDetailRemoteDataSource.getMovieDetailReviews(movieId,nextPage)

            LoadResult.Page(
                data = response.results,
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey = if (nextPage < response.total_pages) response.page.plus(1) else null
            )


        } catch (e: Exception) {
            Timber.d(e)
            LoadResult.Error(throwable = e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MovieDetailReviewDto>): Int? = state.anchorPosition
}