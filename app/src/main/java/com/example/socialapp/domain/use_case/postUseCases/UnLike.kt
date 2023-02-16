package com.example.socialapp.domain.use_case.postUseCases

import com.example.socialapp.domain.repository.PostRepository
import javax.inject.Inject

class UnLike @Inject constructor(
    private val repository: PostRepository
) {
    operator fun invoke(postId: String, userId : String) = repository.unLike(postId, userId)
}
