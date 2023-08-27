package com.example.mymovieapp.features.details.domain.mapper

import com.example.mymovieapp.features.details.data.remote.MovieDetailDto
import com.example.mymovieapp.features.details.data.remote.MovieDetailVideoDto
import com.example.mymovieapp.features.details.data.remote.MovieDetailVideoResponse
import com.example.mymovieapp.features.details.domain.model.MovieDetailVideo
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

class MovieDetailTrailerMapper @Inject constructor() {

    fun mapMovieTrailerDtoToMovieDetailVideo(
        movieDetailTrailerDto : MovieDetailVideoDto
    ) : MovieDetailVideo {
        return MovieDetailVideo(
            type = movieDetailTrailerDto.type,
            name = movieDetailTrailerDto.name,
            date = dateConverter(movieDetailTrailerDto.publishTime),
            key = movieDetailTrailerDto.key
        )
    }

    private fun dateConverter(date : String) : String{
        val formatter = DateTimeFormatter.ISO_ZONED_DATE_TIME
        val dateTime: ZonedDateTime = ZonedDateTime.parse(date, formatter)
        val formatter2: DateTimeFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.US)
        return dateTime.format(formatter2)
    }
}