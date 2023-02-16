package com.example.socialapp.domain.use_case.commentUseCases

import com.example.socialapp.domain.models.Comment
import com.example.socialapp.domain.repository.CommentRepository
import javax.inject.Inject

class SetComment @Inject constructor(
    private val repository: CommentRepository
) {
    operator fun invoke(postId: String, comment: Comment) = repository.setComments(postId, comment)
}