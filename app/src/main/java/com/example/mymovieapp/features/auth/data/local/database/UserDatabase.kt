package com.example.mymovieapp.features.auth.data.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.mymovieapp.features.auth.data.local.entity.UserEntity

@Dao
interface UserDatabase {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewUser(user : UserEntity)

    @Query("SELECT * FROM users WHERE userUid =:userUid")
    suspend fun getUserWithUserUid(userUid : String) : UserEntity

    @Update
    suspend fun updateUser(user : UserEntity)

}