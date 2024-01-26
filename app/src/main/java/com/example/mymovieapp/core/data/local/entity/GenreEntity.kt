package com.example.mymovieapp.core.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mymovieapp.core.data.remote.response.GenreListResponse

@Entity(tableName = "genres")
data class GenreEntity(
    @PrimaryKey(autoGenerate = false) val id : Int,
    @ColumnInfo("genre_name") val name : String
) {
    fun entityToDto() : GenreListResponse.GenreDto = GenreListResponse.GenreDto(
        id = this.id,
        name = this.name
    )
}
