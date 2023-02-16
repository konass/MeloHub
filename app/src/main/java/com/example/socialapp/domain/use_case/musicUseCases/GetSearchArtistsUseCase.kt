package com.example.socialapp.domain.use_case.musicUseCases

import com.example.socialapp.data.remote.dto.Artist
import com.example.socialapp.domain.repository.MusicRepository
import com.example.socialapp.utils.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetSearchArtistsUseCase @Inject constructor(
    private val repository: MusicRepository
) {
    operator fun invoke (artist: String?) : Flow<Response<List<Artist>>> = flow{
        try{
            emit(Response.Loading)
            val response = repository.getSearchArtists(artist).results.artistmatches.artist
            emit(Response.Success(response))
        } catch(e: HttpException) {
            emit(Response.Error(e.localizedMessage ?: "An unexpected error occured"))
        } catch(e: IOException) {
            emit(Response.Error("Couldn't reach server. Check your internet connection."))
        }
    }
}