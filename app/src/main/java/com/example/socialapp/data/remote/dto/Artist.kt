package com.example.socialapp.data.remote.dto

import com.example.socialapp.domain.models.ArtistModel

data class Artist(
    val artist: List<Artist>,
    val mbid: String,
    val name: String,
    val url: String,
    val image: List<Image>,
    val playcount: String,
    val streamable: String,
    val tagcount: String,
    val listeners: String,
)
fun Artist.toArtistModel(): ArtistModel {
    return ArtistModel(
        mbid = mbid,
        name = name,
        url = url,
        image = image.lastIndex.toString()
    )
}
