package com.example.socialapp.presentation.music


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socialapp.data.remote.dto.*
import com.example.socialapp.domain.models.Song
import com.example.socialapp.domain.use_case.musicUseCases.MusicUseCases
import com.example.socialapp.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class MusicViewModel @Inject
constructor(private val musicUseCase: MusicUseCases) : ViewModel()
{
    private val _getAlbumInfoState = MutableStateFlow<Response<ArtistAlbum?>>(Response.Success(null))
    val getAlbumInfoState: StateFlow<Response<ArtistAlbum?>> = _getAlbumInfoState
    private val _getArtistsState = MutableStateFlow<Response<List<ArtistDTO>>>(Response.Success(emptyList()))
    val getArtistsState: StateFlow<Response<List<ArtistDTO>>> = _getArtistsState
    private val _getChartTrackState = MutableStateFlow<Response<List<Track>?>>(Response.Success(null))
    val getChartTrackState: StateFlow<Response<List<Track>?>> = _getChartTrackState
    private val _getTopArtistsState = MutableStateFlow<Response<List<Artist>?>>(Response.Success(null))
    val getTopArtistsState: StateFlow<Response<List<Artist>?>> = _getTopArtistsState
    private val _getSearchTrackState = MutableStateFlow<Response<searchTrack?>>(Response.Success(null))
    val getSearchTrackState: StateFlow<Response<searchTrack?>> = _getSearchTrackState
    private val _getSearchArtistState = MutableStateFlow<Response<List<Artist>>>(Response.Success(emptyList()))
    val getSearchArtistState: StateFlow<Response<List<Artist>>> = _getSearchArtistState
    private val _getTopAlbumState = MutableStateFlow<Response<List<ArtistAlbum>>>(Response.Success(emptyList()))
    val getTopAlbumState: StateFlow<Response<List<ArtistAlbum>>> = _getTopAlbumState
    private val _getTrackInfoState = MutableStateFlow<Response<TrackDto?>>(Response.Success(null))
    val getTrackInfoState: StateFlow<Response<TrackDto?>> = _getTrackInfoState
    private var _getFavoriteSongState = MutableStateFlow<Response<List<Song>>>(Response.Success(emptyList()))
    val getFavoriteSongState: StateFlow<Response<List<Song>>> = _getFavoriteSongState
    fun getAlbumInfo(artist: String?, album: String?) {
        musicUseCase.getAlbumInfoUseCase(artist, album).onEach {
            when(it) {
                is Response.Loading -> _getAlbumInfoState.value = Response.Loading
                is Response.Success -> _getAlbumInfoState.value = Response.Success(it.data)
                is Response.Error -> _getAlbumInfoState.value = Response.Error(it.message)
            }
        }.launchIn(viewModelScope)
    }
    fun getArtists(artist: String?, ) {
        musicUseCase.getArtistsUseCase(artist).onEach {
            when(it) {
                is Response.Loading -> _getArtistsState.value = Response.Loading
                is Response.Success -> _getArtistsState.value = Response.Success(it.data)
                is Response.Error -> _getArtistsState.value = Response.Error(it.message)
            }
        }.launchIn(viewModelScope)
    }
    fun getChartTrack() {
    musicUseCase.getChartTrackUseCase().onEach {
        when(it) {
            is Response.Loading -> _getChartTrackState.value = Response.Loading
            is Response.Success -> _getChartTrackState.value = Response.Success(it.data)
            is Response.Error -> _getChartTrackState.value = Response.Error(it.message)
        }
    }.launchIn(viewModelScope)
}
    fun getTopArtists() {
        musicUseCase.getTopArtists().onEach {
            when(it) {
                is Response.Loading -> _getTopArtistsState.value = Response.Loading
                is Response.Success -> _getTopArtistsState.value = Response.Success(it.data)
                is Response.Error -> _getTopArtistsState.value = Response.Error(it.message)
            }
        }.launchIn(viewModelScope)
    }
    fun getSearchArtists(artist: String?) {
        musicUseCase.getSearchArtistsUseCase(artist).onEach {
            when(it) {
                is Response.Loading -> _getSearchArtistState.value = Response.Loading
                is Response.Success -> _getSearchArtistState.value = Response.Success(it.data)
                is Response.Error -> _getSearchArtistState.value = Response.Error(it.message)
            }
        }.launchIn(viewModelScope)
    }
    fun getSearchTrack(trackName: String?) {
        musicUseCase.getSearchTrackUseCase(trackName).onEach {
            when(it) {
                is Response.Loading -> _getSearchTrackState.value = Response.Loading
                is Response.Success -> _getSearchTrackState.value = Response.Success(it.data)
                is Response.Error -> _getSearchTrackState.value = Response.Error(it.message)
            }
        }.launchIn(viewModelScope)
    }

    fun getTopAlbum(artist: String?, page: String?, limit: String?) {
        musicUseCase.getTopAlbumsUseCase(artist, page, limit).onEach {
            when(it) {
                is Response.Loading -> _getTopAlbumState.value = Response.Loading
                is Response.Success -> _getTopAlbumState.value = Response.Success(it.data)
                is Response.Error -> _getTopAlbumState.value = Response.Error(it.message)
            }
        }.launchIn(viewModelScope)
    }
    fun getTrackInfo(track: String?, artist: String?) {
        musicUseCase.getTrackInfoUseCase(track, artist).onEach {
            when(it) {
                is Response.Loading -> _getTrackInfoState.value = Response.Loading
                is Response.Success -> _getTrackInfoState.value = Response.Success(it.data)
                is Response.Error -> _getTrackInfoState.value = Response.Error(it.message)
            }
        }.launchIn(viewModelScope)
    }
    fun getFavoriteSong() {
        viewModelScope.launch {
            musicUseCase.getFavoriteMusic.invoke().collect{
                _getFavoriteSongState.value=it
            }
        }
    }
    fun saveSong(song: Song){
        viewModelScope.launch {
            musicUseCase.saveFavoriteMusic.invoke(song)
        }
    }
    fun deleteSong(song: Song) {
        viewModelScope.launch {
            musicUseCase.deleteFavoriteMusicUseCase.invoke(song)
        }
    }
}