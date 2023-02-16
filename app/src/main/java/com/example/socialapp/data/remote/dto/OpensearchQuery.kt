package com.example.socialapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class OpensearchQuery(
    @SerializedName("#text")
    val text: String,
    val role: String,
    val startPage: String,
    val searchTerms: String,
)