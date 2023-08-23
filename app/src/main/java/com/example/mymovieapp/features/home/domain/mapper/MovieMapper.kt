package com.example.mymovieapp.features.home.domain.mapper

import com.example.mymovieapp.core.data.remote.response.MovieDto
import com.example.mymovieapp.core.data.remote.response.MovieResponse
import com.example.mymovieapp.features.details.domain.model.MovieDetailSimilarMovie
import com.example.mymovieapp.features.home.domain.model.Movie
import com.example.mymovieapp.utils.ImageApi
import com.example.mymovieapp.utils.extensions.releaseDateToYear
import javax.inject.Inject
import kotlin.math.roundToInt

class MovieMapper @Inject constructor(
) {

    fun mapOnMovieDto(
        movieResponse: MovieDto
    ) : Movie {
        return Movie(
            id = movieResponse.id,
            title = movieResponse.title,
            content = movieResponse.overview,
            image = ImageApi.getImage(
                imageUrl = movieResponse.posterPath
            ),
            backImage = ImageApi.getImage(
                imageUrl = movieResponse.backdropPath
            ),
            genreList = emptyList(),
            voteScore = ((movieResponse.voteAverage * 10.0).roundToInt() / 10.0).toString(),
            releaseYear = movieResponse.releaseDate?.releaseDateToYear() ?: ""
        )
    }

    fun mapMovieDtoToMovieDetailSimilarMovie(
        movieResponse: MovieDto
    ) : MovieDetailSimilarMovie {
        return MovieDetailSimilarMovie(
            id = movieResponse.id,
            title = movieResponse.title,
            content = movieResponse.overview,
            image = ImageApi.getImage(
                imageUrl = movieResponse.posterPath
            ),
            backImage = ImageApi.getImage(
                imageUrl = movieResponse.backdropPath
            ),
            genreList = null,
            movieScore = (((movieResponse.voteAverage * 10.0).roundToInt() / 10.0) / 2).toInt(),
            releaseYear = movieResponse.releaseDate?.releaseDateToYear() ?: ""
        )
    }
}