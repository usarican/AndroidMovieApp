package com.example.mymovieapp.features.home.domain.mapper

import com.example.mymovieapp.core.data.remote.response.MovieDto
import com.example.mymovieapp.core.data.remote.response.MovieResponse
import com.example.mymovieapp.features.home.domain.model.Movie
import javax.inject.Inject

class MovieMapper @Inject constructor(
) {

    fun mapOnMovieDto(
        movieResponse: MovieDto
    ) : Movie {
        return Movie(
            id = movieResponse.id,
            title = movieResponse.title,
            content = movieResponse.overview,
            image = movieResponse.posterPath,
            genreList = emptyList(),
            voteScore = movieResponse.voteAverage
        )
    }
}