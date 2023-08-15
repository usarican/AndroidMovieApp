package com.example.mymovieapp.features.details.domain.mapper

import com.example.mymovieapp.core.data.remote.response.GenreListResponse
import javax.inject.Inject

class DetailsGenreListMapper @Inject constructor() {

    fun mapToGenreDtoToGenreList(genreDtoList : List<GenreListResponse.GenreDto>) : List<String> {
        return genreDtoList.map { genreDto -> genreDto.name }
    }
}