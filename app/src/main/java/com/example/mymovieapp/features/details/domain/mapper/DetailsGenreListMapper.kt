package com.example.mymovieapp.features.details.domain.mapper

import com.example.mymovieapp.core.data.remote.response.GenreListResponse
import com.example.mymovieapp.core.data.remote.response.GenreResponse
import javax.inject.Inject

class DetailsGenreListMapper @Inject constructor() {

    fun mapToGenreDtoToGenreList(genreResponseList : List<GenreResponse>) : List<String> {
        return genreResponseList.map { genreResponse -> genreResponse.name }
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