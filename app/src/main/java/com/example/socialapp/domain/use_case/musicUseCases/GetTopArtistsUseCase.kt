package com.example.socialapp.domain.use_case.musicUseCases

import com.example.socialapp.data.remote.dto.Artist
import com.example.socialapp.domain.repository.MusicRepository
import com.example.socialapp.utils.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetTopArtistsUseCase @Inject constructor(
    private val repository : MusicRepository
) {
    operator fun invoke () : Flow<Response<List<Artist>>> = flow{
        try{
            emit(Response.Loading)
            val response = repository.getTopArtists().artists.artist
              emit(Response.Success(response))
        } catch(e: HttpException) {
            emit(Response.Error(e.localizedMessage ?: "An unexpected error occured"))
        } catch(e: IOException) {
            emit(Response.Error(e.localizedMessage?: "reach internet permission"))
        }
    }
}