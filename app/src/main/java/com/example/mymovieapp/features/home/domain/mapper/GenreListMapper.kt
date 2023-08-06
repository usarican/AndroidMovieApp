package com.example.mymovieapp.features.home.domain.mapper

import com.example.mymovieapp.core.data.remote.response.GenreListResponse
import javax.inject.Inject

class GenreListMapper @Inject constructor() {

    fun mapOnGenresToMap(
        genreListResponse: GenreListResponse
    ) : Map<Int,String> {
        return genreListResponse.genres.associate { it.id to it.name }
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