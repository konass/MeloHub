package com.example.socialapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class Album(
    @SerializedName("@attr")
    val attr: Attr,
    val artist: String,
    val image: List<Image>,
    val mbid: String,
    val title: String,
    val listeners: String,
    val name: String,
    val playcount: String,
    val tags: Tags,
    val tracks: Tracks,
    val url: String,
    val wiki: Wiki
)