package com.example.socialapp.domain.repository

import com.example.socialapp.data.remote.dto.*
import com.example.socialapp.domain.models.Song


interface MusicRepository {
    suspend fun getArtists(artist: String?): List<ArtistDTO>
    suspend fun getSearchArtists (artist: String?): SearchArtist
    suspend fun getTopAlbums (artist: String?, page: String?, limit: String?): List<ArtistAlbum>
    suspend fun getAlbumInfo ( artist: String? , album : String?): ArtistAlbum
    suspend fun getTrackInfo (track: String?, artist: String?): TrackDto
    suspend fun getChartTrack (): ChartTrack
    suspend fun getSearchTrack (trackName : String?): searchTrack
    suspend fun getTopArtists(): TopArtists
   suspend fun getFavoriteSong():List<Song>
    suspend fun addToFavorite(song: Song)
    suspend fun deleteFromFavorite (song: Song)
}