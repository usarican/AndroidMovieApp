package com.example.mymovieapp.features.details.domain.mapper

import com.example.mymovieapp.features.details.data.remote.MovieDetailDto
import com.example.mymovieapp.features.details.domain.model.MovieDetail
import com.example.mymovieapp.features.details.domain.model.MovieDetailPageItem
import com.example.mymovieapp.utils.ImageApi
import javax.inject.Inject
import kotlin.math.roundToInt

class MovieDetailMapper @Inject constructor(){

    fun mapOnMovieDetailDtoToMovieDetail(movieDetailDto: MovieDetailDto) : MovieDetail {
        return MovieDetail(
            id = movieDetailDto.id,
            movieTitle = movieDetailDto.title,
            content = movieDetailDto.content,
            movieTime = movieTimeIntToStringConverter(movieDetailDto.movieTime),
            image = ImageApi.getImage(
                imageUrl = movieDetailDto.posterPath
            ),
            releaseDate = movieDetailDto.releaseDate,
            genres = emptyList(),
            voteCount = movieDetailDto.voteCount,
            movieScore = movieDetailDto.voteAverage,
            status = movieDetailDto.status
        )
    }

    fun mapOnMovieDetailDtoToMovieDetailPageItem(movieDetailDto: MovieDetailDto) : MovieDetailPageItem {
        return MovieDetailPageItem(
            id = movieDetailDto.id,
            movieTitle = movieDetailDto.title,
            content = movieDetailDto.content,
            movieTime = movieTimeIntToStringConverter(movieDetailDto.movieTime),
            posterImage = ImageApi.getImage(
                imageUrl = movieDetailDto.posterPath
            ),
            backgroundImage = ImageApi.getImage(
                imageUrl = movieDetailDto.backdropPath
            ),
            releaseDate = movieDetailDto.releaseDate?.take(4),
            genres = emptyList(),
            voteCount = "(${movieDetailDto.voteCount} Reviews)",
            movieScore = ((movieDetailDto.voteAverage * 10.0).roundToInt() / 10.0).toString(),
            status = movieDetailDto.status,
            productionCountry = if (movieDetailDto.productionCountries.isNotEmpty()) movieDetailDto.productionCountries[0] else null
        )
    }

    private fun movieTimeIntToStringConverter(movieRuntime : Int) : String {
        val movieRuntimeHour = (movieRuntime  / 60)
        val movieRuntimeMinute = movieRuntime - (movieRuntimeHour * 60)
        return "${movieRuntimeHour}h ${movieRuntimeMinute}m"
    }
}