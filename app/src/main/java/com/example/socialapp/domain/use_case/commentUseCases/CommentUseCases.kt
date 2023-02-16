package com.example.socialapp.domain.use_case.commentUseCases

data class CommentUseCases(
    val setComment: SetComment,
    val getComments: GetComments,
    val getAuthor: GetAuthor,
    val updateComment: UpdateComment,
    val  getPostForCommentById :  GetPostForCommentById
)
