package com.example.mymovieapp.features.details.domain.mapper

import com.example.mymovieapp.features.details.data.remote.MovieDetailDto
import com.example.mymovieapp.features.details.domain.model.MovieDetail
import com.example.mymovieapp.utils.ImageApi
import javax.inject.Inject

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

    private fun movieTimeIntToStringConverter(movieRuntime : Int) : String {
        val movieRuntimeHour = (movieRuntime  / 60)
        val movieRuntimeMinute = movieRuntime - (movieRuntimeHour * 60)
        return "${movieRuntimeHour}h ${movieRuntimeMinute}m"
    }
}