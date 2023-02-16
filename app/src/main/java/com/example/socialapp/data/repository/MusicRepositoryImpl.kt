package com.example.socialapp.data.repository


import com.example.socialapp.data.local.db.SongDao
import com.example.socialapp.data.remote.MusicApi
import com.example.socialapp.data.remote.dto.*
import com.example.socialapp.domain.models.Song
import com.example.socialapp.domain.repository.MusicRepository
import javax.inject.Inject

class MusicRepositoryImpl @Inject constructor(
    private val musicApi: MusicApi,
    private val songDao: SongDao
): MusicRepository {
    override suspend fun getArtists(artist: String?): List<ArtistDTO> {
        return musicApi.getArtists(artist)
    }
    override suspend fun getSearchArtists (artist: String?) : SearchArtist {
        return musicApi.getSearchArtists(artist)
    }
    override suspend fun getTopAlbums (artist: String?, page: String?, limit: String?): List<ArtistAlbum> {
        return musicApi.getTopAlbums(artist, page, limit)
    }
    override suspend fun getAlbumInfo ( artist: String? , album : String?): ArtistAlbum {
        return musicApi.getAlbumInfo(artist, album)
    }
    override suspend fun getTrackInfo (track: String?, artist: String?): TrackDto {
        return musicApi.getTrackInfo(track, artist)
    }
    override suspend fun getChartTrack (): ChartTrack {
        return musicApi.getChartTopTracks()
    }
    override suspend fun getSearchTrack (trackName : String?): searchTrack {
        return musicApi.getSearchTracks(trackName)
    }
    override suspend fun getTopArtists() : TopArtists{
        return musicApi.getChartTopArtists()
    }
   override suspend fun getFavoriteSong(): List<Song> =  songDao.getAllSong()
    override suspend fun addToFavorite(song: Song) =  songDao.insert(song=song)
   override suspend fun deleteFromFavorite (song: Song) = songDao.delete(song=song)
}