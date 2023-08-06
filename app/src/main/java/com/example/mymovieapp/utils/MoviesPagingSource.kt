package com.example.mymovieapp.utils

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.mymovieapp.core.data.remote.response.MovieDto
import com.example.mymovieapp.core.data.remote.response.MovieResponse
import com.example.mymovieapp.utils.Constants.STARTING_PAGE
import timber.log.Timber
import javax.inject.Inject

class MoviesPagingSource @Inject constructor(
    private val getMoviesFunc : suspend (Int) -> MovieResponse
) : PagingSource<Int,MovieDto>(){

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieDto> {
        val nextPage = params.key ?: STARTING_PAGE
        return try {
            val response = getMoviesFunc(nextPage)

            LoadResult.Page(
                data = response.results,
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey = if (nextPage < response.totalPages) response.page.plus(1) else null
            )


        } catch (e: Exception) {
            Timber.d(e)
            LoadResult.Error(throwable = e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MovieDto>): Int? = state.anchorPosition

}