package com.example.socialapp.domain.use_case.postUseCases

import com.example.socialapp.domain.repository.PostRepository
import com.google.android.gms.tasks.Task
import javax.inject.Inject

class SetUpdatePostData @Inject constructor(
    private val repository: PostRepository
) {

    operator fun invoke(likedBy:  ArrayList<String>?, postId:String): Task<Void> {
        val postObj = mutableMapOf<String, ArrayList<String>?>()
        postObj["likedBy"]= likedBy
        return repository.setUpdatePostData(postObj, postId)

    }    }