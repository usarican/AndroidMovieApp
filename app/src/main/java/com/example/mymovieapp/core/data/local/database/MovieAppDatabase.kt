package com.example.mymovieapp.core.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mymovieapp.core.data.local.entity.GenreEntity
import com.example.mymovieapp.features.auth.data.local.database.UserDatabase
import com.example.mymovieapp.features.auth.data.local.entity.UserEntity

@Database(
    entities = [
        GenreEntity::class,
        UserEntity::class
    ], version = 2, exportSchema = true
)
abstract class MovieAppDatabase : RoomDatabase() {
    abstract val genreDatabase: GenreDatabase
    abstract val userDatabase: UserDatabase
}