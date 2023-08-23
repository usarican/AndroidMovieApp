package com.example.mymovieapp.features.details.domain.mapper

import com.example.mymovieapp.core.data.remote.response.GenreListResponse
import javax.inject.Inject

class DetailsGenreListMapper @Inject constructor() {

    fun mapToGenreDtoToGenreList(genreDtoList : List<GenreListResponse.GenreDto>) : List<String> {
        return genreDtoList.map { genreDto -> genreDto.name }
    }

    fun genreListSeparateWithBar(genres : List<String?>) : String {
        var string = ""
        genres.forEachIndexed { index, s ->
            string += if (index < genres.size - 1){
                ("$s | ")
            } else {
                s
            }
        }
        return string
    }
}