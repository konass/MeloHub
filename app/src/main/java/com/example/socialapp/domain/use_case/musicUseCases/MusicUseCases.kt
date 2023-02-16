package com.example.socialapp.domain.use_case.musicUseCases

data class MusicUseCases (
    val getAlbumInfoUseCase: GetAlbumInfoUseCase,
    val getArtistsUseCase: GetArtistsUseCase,
    val getChartTrackUseCase: GetChartTrackUseCase,
    val getSearchArtistsUseCase: GetSearchArtistsUseCase,
    val getTopAlbumsUseCase: GetTopAlbumsUseCase,
    val getTrackInfoUseCase: GetTrackInfoUseCase,
    val getSearchTrackUseCase: GetSearchTrackUseCase,
    val getTopArtists: GetTopArtistsUseCase,
    val getFavoriteMusic: GetFavoriteMusic,
    val saveFavoriteMusic: SaveFavoriteMusic,
    val deleteFavoriteMusicUseCase: DeleteFavoriteMusicUseCase
        )