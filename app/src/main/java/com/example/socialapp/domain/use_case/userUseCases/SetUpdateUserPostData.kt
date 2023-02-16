package com.example.socialapp.domain.use_case.userUseCases

import com.example.socialapp.domain.repository.UserRepository
import com.google.android.gms.tasks.Task
import javax.inject.Inject

class SetUpdateUserPostData @Inject constructor(
    private val repository: UserRepository
) {

    operator fun invoke(likedBy:  ArrayList<String>?, postId:String): Task<Void> {
        val postObj = mutableMapOf<String, ArrayList<String>?>()
        postObj["likedBy"]= likedBy
        return repository.setUpdatePostData(postObj, postId)

    }    }