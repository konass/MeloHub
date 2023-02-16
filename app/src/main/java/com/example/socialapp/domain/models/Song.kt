package com.example.socialapp.domain.models

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity(tableName = "songs")
data class Song(
    @PrimaryKey()
    val mediaId: String = "",
    val title: String ="",
    val subtitle: String = "",
    val songUrl: String= "",
    val imageUrl: String="",
) : Serializable
