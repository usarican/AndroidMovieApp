package com.example.mymovieapp.core.data.remote.response

import com.example.mymovieapp.core.data.local.entity.GenreEntity

data class GenreListResponse(
    val genres : List<GenreDto>
) {
    data class GenreDto(
        val id : Int,
        val name : String
    ) {
        fun dtoToEntity() : GenreEntity = GenreEntity(
            id = this.id,
            name = this.name
        )
    }
}