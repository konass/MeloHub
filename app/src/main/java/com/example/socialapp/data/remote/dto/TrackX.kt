package com.example.socialapp.data.remote.dto

import com.example.socialapp.domain.models.Song
import com.example.socialapp.domain.models.TrackModel
import com.google.gson.annotations.SerializedName
import java.util.*

data class TrackX (
    val album: Album,
    val toptags: Toptags,
    val wiki: Wiki,
    @SerializedName("@attr")
    val attr: Attr,
    val artist: String,
    val duration: String?,
    val image: List<Image>,
    val listeners: String?,
    val mbid: String?,
    val name: String?,
    val playcount: String?,
    val streamable: String?,
    val url: String?,
        )
