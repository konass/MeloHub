package com.example.socialapp.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.socialapp.domain.models.Song

@Database(entities=[Song::class], version = 1, exportSchema = true)
abstract class SongDatabase : RoomDatabase() {
    abstract fun getSongDao() : SongDao

}