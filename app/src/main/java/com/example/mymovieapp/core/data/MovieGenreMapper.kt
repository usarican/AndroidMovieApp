package com.example.mymovieapp.core.data
import com.example.mymovieapp.core.data.local.entity.GenreEntity
import com.example.mymovieapp.core.data.remote.response.GenreResponse
import javax.inject.Inject

class MovieGenreMapper @Inject constructor() {
    fun mapToEntity(
        response: GenreResponse
    ) : GenreEntity =
        with(response) {
            GenreEntity(
                id = id,
                name = name
            )
        }

    fun mapToResponse(
        entity: GenreEntity
    ) : GenreResponse =
        with(entity) {
            GenreResponse(
                id = id,
                name = name
            )
        }
}