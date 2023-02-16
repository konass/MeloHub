package com.example.socialapp.domain.use_case.commentUseCases

import com.example.socialapp.domain.repository.CommentRepository
import javax.inject.Inject

class GetPostForCommentById @Inject constructor(
    private val repository : CommentRepository
) {
    suspend operator fun invoke (postId: String) = repository.getPostById(postId)
}