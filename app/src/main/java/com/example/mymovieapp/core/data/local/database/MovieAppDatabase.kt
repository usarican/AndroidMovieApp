package com.example.mymovieapp.core.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mymovieapp.core.data.local.entity.GenreEntity

@Database(
    entities = [
        GenreEntity::class
    ], version = 1, exportSchema = true
)
abstract class MovieAppDatabase : RoomDatabase() {
    abstract val genreDatabase : GenreDatabase
}