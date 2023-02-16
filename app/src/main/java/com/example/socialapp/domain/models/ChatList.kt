package com.example.socialapp.domain.models

import android.os.Parcel
import android.os.Parcelable

data class ChatList(
    val senderId: String? ="",
    val message: Message?= null,
    val user: User? = null,
        ): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readParcelable(Message::class.java.classLoader),
        TODO("user")) {
    }

    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }


    override fun writeToParcel(p0: Parcel, p1: Int) {
        TODO("Not yet implemented")
    }

    companion object CREATOR : Parcelable.Creator<ChatList> {
        override fun createFromParcel(parcel: Parcel): ChatList {
            return ChatList(parcel)
        }

        override fun newArray(size: Int): Array<ChatList?> {
            return arrayOfNulls(size)
        }
    }
}