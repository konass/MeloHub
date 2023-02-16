package com.example.socialapp.data.local.db

import androidx.room.*
import com.example.socialapp.domain.models.Song

@Dao
interface SongDao {
    @Query("SELECT * FROM songs")
   suspend fun getAllSong(): List<Song>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(song : Song)
    @Delete
    suspend fun delete (song : Song)
}