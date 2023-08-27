package com.example.mymovieapp.features.details.domain.mapper

import com.example.mymovieapp.features.details.data.remote.MovieDetailReviewDto
import com.example.mymovieapp.features.details.domain.model.MovieDetailReview
import com.example.mymovieapp.utils.ImageApi
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

class MovieDetailReviewMapper @Inject constructor() {
    fun mapOnReviewDtoToMovieDetailReview(movieDetailReviewDto: MovieDetailReviewDto) : MovieDetailReview {
        return MovieDetailReview(
            userName = movieDetailReviewDto.author,
            userProfilePicture = ImageApi.getImage(
                imageUrl = movieDetailReviewDto.author_details.authorProfilePicture
            ),
            userComment = movieDetailReviewDto.content,
            userMovieRate = movieDetailReviewDto.author_details.authorRate?.div(2),
            createdDate = dateConverter(movieDetailReviewDto.createDate)
        )
    }

    private fun dateConverter(date : String) : String{
        val formatter = DateTimeFormatter.ISO_ZONED_DATE_TIME
        val dateTime: ZonedDateTime = ZonedDateTime.parse(date, formatter)
        val formatter2: DateTimeFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.US)
        return dateTime.format(formatter2)
    }
}