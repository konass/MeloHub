package com.example.socialapp.domain.use_case.commentUseCases

import com.example.socialapp.domain.repository.CommentRepository
import com.google.android.gms.tasks.Task
import javax.inject.Inject

class UpdateComment @Inject constructor(
    private val repository: CommentRepository
) {

    operator fun invoke( listComment : ArrayList<String>, postId:String): Task<Void> {

            val postObj = mutableMapOf<String,ArrayList<String>>()
            postObj["comments"]= listComment
           return repository.updatePost(postObj, postId)

    }
}