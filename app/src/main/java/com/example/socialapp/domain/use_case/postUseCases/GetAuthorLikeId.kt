package com.example.socialapp.domain.use_case.postUseCases

import com.example.socialapp.domain.repository.PostRepository
import javax.inject.Inject

class GetAuthorLikeId @Inject constructor(
    private val repository : PostRepository
) {
    operator fun invoke () = repository.getUserId()
}