package com.example.socialapp.data.remote.dto

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Artists(
    @SerializedName("@attr")
    val attr: Attr,
    val artist: List<Artist>
): Serializable