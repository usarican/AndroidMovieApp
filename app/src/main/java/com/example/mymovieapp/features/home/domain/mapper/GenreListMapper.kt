package com.example.mymovieapp.features.home.domain.mapper

import com.example.mymovieapp.core.data.remote.response.GenreListDto
import javax.inject.Inject

class GenreListMapper @Inject constructor() {

    fun mapOnGenresToMap(
        genreListResponse: GenreListDto
    ) : Map<Int,String> {
        return genreListResponse.genres.map { it.id to it.name }.toMap()
    }

    fun mapOnGenreListKeyToValue(
        genreListMap : Map<Int,String>,
        genreKeys : List<Int>
    ) : List<String?> {
        val genreValues = mutableListOf<String?>()
        genreKeys.forEach {
            genreValues.add(genreListMap[it])
        }
        return genreValues
    }
}