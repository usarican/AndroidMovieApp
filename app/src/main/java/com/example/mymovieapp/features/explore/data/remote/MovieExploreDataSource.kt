package com.example.mymovieapp.features.explore.data.remote

import com.example.mymovieapp.core.data.remote.response.MovieResponse
import javax.inject.Inject

class MovieExploreDataSource @Inject constructor(
    private val movieSearchService: MovieSearchService
) {

    suspend fun getSearchingMovieResult(page : Int,searchingQuery : String) : MovieResponse {
        return movieSearchService.getSearchingResult(page = page, searchString = searchingQuery)
    }
    suspend fun getDiscoveryMovieResult(
        page : Int,
        language : String,
        sortFilter : String?,
        genreListFilter : String?,
        regionFilter : String?,
        yearFilter : Int?
    ) : MovieResponse = movieSearchService.getDiscoveryMovieResult(
        page = page,
        language = language,
        sortFilter = sortFilter,
        genreListFilter = genreListFilter,
        regionFilter = regionFilter,
        yearFilter = yearFilter
    )
}