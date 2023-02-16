package com.example.socialapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class Tracks(
    @SerializedName("@attr")
    val attr: Attr,
    val track: List<Track>
)