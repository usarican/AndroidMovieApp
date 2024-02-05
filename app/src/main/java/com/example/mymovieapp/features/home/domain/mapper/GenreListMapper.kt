package com.example.mymovieapp.features.home.domain.mapper

import com.example.mymovieapp.core.data.remote.response.GenreListResponse
import com.example.mymovieapp.core.data.remote.response.GenreResponse
import javax.inject.Inject

class GenreListMapper @Inject constructor() {

    private fun genreListMapOnIdToList(
        genreResponseList: List<GenreResponse>
    ) : Map<Int,String> {
        return genreResponseList.associate { it.id to it.name }
    }

    fun mapOnGenreListKeyToValue(
        genreResponseList : List<GenreResponse>,
        genreKeys : List<Int>
    ) : List<String?> {
        val genreListMap = genreListMapOnIdToList(genreResponseList)
        val genreValues = mutableListOf<String?>()
        genreKeys.forEach {
            genreValues.add(genreListMap[it])
        }
        return genreValues
    }
}