package com.example.mymovieapp.features.explore.data.remote

import com.example.mymovieapp.core.data.remote.response.MovieResponse
import javax.inject.Inject

class MovieExploreDataSource @Inject constructor(
    private val movieSearchService: MovieSearchService
) {

    suspend fun getSearchingMovieResult(page : Int,searchingQuery : String) : MovieResponse {
        return movieSearchService.getSearchingResult(page = page, searchString = searchingQuery)
    }
}