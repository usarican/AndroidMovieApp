package com.example.mymovieapp.core.data.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomWarnings
import com.example.mymovieapp.core.data.local.entity.GenreEntity
import retrofit2.http.DELETE

@Dao
interface GenreDatabase {

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM genres")
    suspend fun getGenreList() : List<GenreEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGenreList(newList : List<GenreEntity>)

}