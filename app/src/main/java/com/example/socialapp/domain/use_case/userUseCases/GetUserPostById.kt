package com.example.socialapp.domain.use_case.userUseCases

import com.example.socialapp.domain.repository.UserRepository
import javax.inject.Inject

class GetUserPostById @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke (postId: String) = repository.getPostById(postId)
}
