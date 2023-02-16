package com.example.socialapp.domain.use_case.postUseCases

import com.example.socialapp.domain.repository.PostRepository
import javax.inject.Inject

class GetAuthorLikeById @Inject constructor(
  private val  repository: PostRepository
) {
    suspend operator fun invoke(userId : String) = repository.getAuthorLikeById(userId)
}