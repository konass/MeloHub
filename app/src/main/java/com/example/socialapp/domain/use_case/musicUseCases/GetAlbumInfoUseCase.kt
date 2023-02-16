package com.example.socialapp.domain.use_case.musicUseCases

import com.example.socialapp.data.remote.dto.ArtistAlbum
import com.example.socialapp.domain.repository.MusicRepository
import com.example.socialapp.utils.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetAlbumInfoUseCase @Inject constructor(
    private val repository: MusicRepository
) {

    operator fun invoke (artist: String?, album: String?) : Flow<Response<ArtistAlbum>> = flow{
        try{
            emit(Response.Loading)
            val response = repository.getAlbumInfo(artist,album)
            emit(Response.Success(response))
        } catch(e: HttpException) {
            emit(Response.Error(e.localizedMessage ?: "An unexpected error occured"))
        } catch(e: IOException) {
            emit(Response.Error("Couldn't reach server. Check your internet connection."))
        }
    }

}