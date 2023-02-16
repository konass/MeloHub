package com.example.socialapp.domain.use_case.musicUseCases

import com.example.socialapp.domain.models.Song
import com.example.socialapp.domain.repository.MusicRepository
import com.example.socialapp.utils.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetFavoriteMusic @Inject
constructor(private val repository: MusicRepository){
   operator fun invoke(): Flow<Response<List<Song>>> = flow{
      try {
         emit(Response.Loading)
         val  songs = repository.getFavoriteSong()
         emit(Response.Success((songs)))
      }catch (e: Exception) {
         emit(Response.Error(e.localizedMessage ?: " An Unexpected Error"))
      }
   }
}