package com.example.socialapp.domain.models

import java.io.Serializable

data class User(
    var userId: String = "",
    var password: String= "",
    var email: String= "",
    var name: String= "",
    var lastName: String= "",
    var nickName: String= "",
    var imageUrl: String= "",
    var description: String="",
    var nfollowers: ArrayList<String> = ArrayList(),
    var nfollowing: ArrayList<String> = ArrayList()
): Serializable