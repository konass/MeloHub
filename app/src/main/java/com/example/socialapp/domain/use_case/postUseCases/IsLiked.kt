package com.example.socialapp.domain.use_case.postUseCases

import com.example.socialapp.domain.models.User
import com.example.socialapp.domain.repository.PostRepository
import javax.inject.Inject

class IsLiked @Inject constructor(
    private val repository: PostRepository
) {
    operator fun invoke(postId : String, user: User) = repository.isLiked(postId, user)
}