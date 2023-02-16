package com.example.socialapp.domain.models

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

data class Post(
    var postId: String? = null,
    var imageUrl: String? = null,
    //var music: Music,
    var author: User? = null,
    var postText: String? = null,
    var time: Long?=null,
    var userId: String? = null,
    var nLikes: Int? = null,
    val likedBy: ArrayList<String> = ArrayList(),
    var likes: List<User>? = null,
    var comments: ArrayList<String> = ArrayList(),
    var music: Music? = null
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        TODO("author"),
        parcel.readString(),
        parcel.readValue(Long::class.java.classLoader) as? Long,
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        TODO("likedBy"),
        TODO("likes"),
        TODO("comments"),
        TODO("music")) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(postId)
        parcel.writeString(imageUrl)
        parcel.writeString(postText)
        parcel.writeValue(time)
        parcel.writeString(userId)
        parcel.writeValue(nLikes)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Post> {
        override fun createFromParcel(parcel: Parcel): Post {
            return Post(parcel)
        }

        override fun newArray(size: Int): Array<Post?> {
            return arrayOfNulls(size)
        }
    }
}