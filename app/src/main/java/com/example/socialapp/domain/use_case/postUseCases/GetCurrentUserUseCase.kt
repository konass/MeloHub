package com.example.socialapp.domain.use_case.postUseCases

import com.example.socialapp.domain.repository.PostRepository
import javax.inject.Inject

class GetCurrentUserUseCase @Inject
constructor(private val repository: PostRepository){

   suspend operator fun invoke(userId: String) = repository.getCurrentUserData(userId)
}