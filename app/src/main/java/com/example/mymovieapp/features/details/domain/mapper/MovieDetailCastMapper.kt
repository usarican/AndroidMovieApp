package com.example.mymovieapp.features.details.domain.mapper

import com.example.mymovieapp.features.details.data.remote.MovieDetailCastDto
import com.example.mymovieapp.features.details.domain.model.MovieDetailCast
import com.example.mymovieapp.utils.ImageApi
import javax.inject.Inject

class MovieDetailCastMapper @Inject constructor() {

    fun mapOnCastDtoToMovieDetailCast(movieDetailCastDto: MovieDetailCastDto) : MovieDetailCast {
        return MovieDetailCast(
            castName = movieDetailCastDto.name,
            castCharacterName = movieDetailCastDto.characterName,
            castProfilePicture = ImageApi.getImage(
                imageUrl = movieDetailCastDto.profilePicture
            )
        )
    }
}