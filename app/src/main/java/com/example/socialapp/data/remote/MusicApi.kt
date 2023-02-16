package com.example.socialapp.data.remote

import com.example.socialapp.BuildConfig

import com.example.socialapp.data.remote.dto.*
import retrofit2.http.GET
import retrofit2.http.Query


interface MusicApi {
    @GET("?method=library.getartists")
   suspend fun getArtists(@Query("user") artist: String?,
                          @Query("api_key") apiKey: String =  BuildConfig.API_KEY,
                          @Query("format") format: String = "json"
    ): List<ArtistDTO>

    @GET("?method=artist.search")
   suspend fun getSearchArtists(@Query("artist") artist: String?,
                                @Query("api_key") apiKey: String = BuildConfig.API_KEY,
                                @Query("format") format: String = "json"
    ): SearchArtist

    @GET("?method=artist.gettopalbums")
   suspend fun getTopAlbums(
        @Query("artist") artist: String?,
        @Query("page") page: String?, @Query("limit") limit: String?,
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Query("format") format: String = "json"
    ): List<ArtistAlbum>

    @GET("?method=album.getinfo")
   suspend fun getAlbumInfo(
        @Query("artist") artist: String?,
        @Query("album") album: String?,
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Query("format") format: String = "json"
    ): ArtistAlbum
    @GET("?method=track.getInfo")
   suspend fun getTrackInfo(
        @Query("track") track: String?,
        @Query("artist") artist: String?,
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Query("format") format: String = "json"
    ): TrackDto
   @GET("?method=chart.gettoptracks")
  suspend fun getChartTopTracks(
           @Query("api_key") apiKey: String = BuildConfig.API_KEY,
           @Query("format") format: String = "json"
   ): ChartTrack
  @GET("?method=track.search")
  suspend fun getSearchTracks(
      @Query("track") trackName: String?,
      @Query("api_key") apiKey: String = BuildConfig.API_KEY,
      @Query("format") format: String = "json"
  ):searchTrack
    @GET("?method=chart.gettopartists")
    suspend fun getChartTopArtists(
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Query("format") format: String = "json"
    ):TopArtists
}