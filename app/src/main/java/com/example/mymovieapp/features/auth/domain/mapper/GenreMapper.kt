package com.example.mymovieapp.features.auth.domain.mapper

import com.example.mymovieapp.features.auth.data.remote.InterestGenreItemDto
import com.example.mymovieapp.features.auth.data.remote.InterestGenreItemResponse
import com.example.mymovieapp.features.explore.ui.dialog.MovieFilterDialogItem
import javax.inject.Inject

class GenreMapper @Inject constructor() {
    fun movieFilterDialogItemToInterestGenreItemDto(item : MovieFilterDialogItem) : InterestGenreItemDto =
        with(item){
            InterestGenreItemDto(
                genreId = id,
                genreName = itemName
            )
        }

    fun interestGenreItemDtoToResponse(interestGenreItemDto: InterestGenreItemDto) : InterestGenreItemResponse =
        with(interestGenreItemDto){
            InterestGenreItemResponse(
                genreId = genreId,
                genreName = genreName
            )
        }
}