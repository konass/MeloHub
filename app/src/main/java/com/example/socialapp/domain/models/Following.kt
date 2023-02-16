package com.example.socialapp.domain.models

data class Following(
    var user : User? = null,
    var isFollow: Boolean= false,
    var userId: String = ""
)
