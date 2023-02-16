package com.example.socialapp.domain.use_case.commentUseCases

import com.example.socialapp.domain.repository.CommentRepository
import javax.inject.Inject

class GetComments @Inject constructor(
private val repository: CommentRepository
) {
    operator fun invoke (postId: String) = repository.getComments(postId)
}