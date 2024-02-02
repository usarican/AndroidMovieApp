package com.example.mymovieapp.core.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "genres")
data class GenreEntity(
    @PrimaryKey(autoGenerate = false) val id : Int,
    @ColumnInfo("genre_name") val name : String
)
