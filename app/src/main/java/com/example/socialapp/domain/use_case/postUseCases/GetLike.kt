package com.example.socialapp.domain.use_case.postUseCases

import com.example.socialapp.domain.repository.PostRepository
import javax.inject.Inject

class GetLike @Inject constructor(
    private val repository : PostRepository
) {
    operator fun invoke (postId: String) = repository.getLike(postId)
}