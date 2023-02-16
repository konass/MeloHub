package com.example.socialapp.domain.models

data class Comment(
    var author: User? = null,
    val text: String = "",
    val time: String = ""
)
