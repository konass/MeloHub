package com.example.socialapp.domain.use_case.userUseCases

import com.example.socialapp.domain.repository.UserRepository
import javax.inject.Inject

class DeletePostUseCase @Inject constructor(
    private val repository : UserRepository
) {
    operator fun invoke (postId : String)= repository.deletePost(postId)
}

