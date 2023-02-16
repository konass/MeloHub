package com.example.socialapp.data.remote.dto

import android.os.Parcel
import android.os.Parcelable
import com.example.socialapp.domain.models.ArtistModel
import com.example.socialapp.domain.models.Song
import com.example.socialapp.domain.models.TrackModel
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

data class Track(
    val track: List<Track>,
    val album: Album,
    val toptags: Toptags,
    val wiki: Wiki,
    @SerializedName("@attr")
    val attr: Attr,
    val artist: Artist,
    val duration: String?,
    val image: List<Image>,
    val listeners: String?,
    val mbid: String?,
    val name: String?,
    val playcount: String?,
    val streamable: Streamable,
    val url: String?,

    ): Serializable

fun Track.toTrackModel(): TrackModel {
    return TrackModel(
        mbid = mbid,
        name = name,
        url = url,
        image = image.lastIndex.toString()
    )
}