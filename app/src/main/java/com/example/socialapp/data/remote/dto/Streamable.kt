package com.example.socialapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class Streamable(
    @SerializedName("#text")
    val text: String,
    val fulltrack: String
)