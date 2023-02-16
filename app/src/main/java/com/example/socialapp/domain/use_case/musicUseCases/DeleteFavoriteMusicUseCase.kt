package com.example.socialapp.domain.use_case.musicUseCases

import com.example.socialapp.domain.models.Song
import com.example.socialapp.domain.repository.MusicRepository
import javax.inject.Inject

class DeleteFavoriteMusicUseCase @Inject constructor(
    private val repository: MusicRepository
) {
suspend operator fun invoke(song: Song) = repository.deleteFromFavorite(song)
}