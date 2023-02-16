package com.example.socialapp.domain.models

import com.example.socialapp.data.remote.dto.*
import com.google.gson.annotations.SerializedName

data class TrackModel (
    val image: String,
    val mbid: String?,
    val name: String?,
    val url: String?,
)