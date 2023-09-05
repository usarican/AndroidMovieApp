package com.example.mymovieapp.features.explore.data

import androidx.paging.PagingData
import com.example.mymovieapp.core.data.remote.response.MovieDto
import kotlinx.coroutines.flow.Flow

interface MovieExploreRepository {

    fun getSearchingMovieResults(
        searchingQuery : String
    ) : Flow<PagingData<MovieDto>>

    fun getDiscoveryMovieResults(
        language : String,
        sortFilter : String?,
        genreListFilter : String?,
        regionFilter : String?,
        yearFilter : Int?
    ) : Flow<PagingData<MovieDto>>
}