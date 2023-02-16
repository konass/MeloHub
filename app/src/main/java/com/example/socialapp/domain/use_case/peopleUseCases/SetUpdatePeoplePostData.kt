package com.example.socialapp.domain.use_case.peopleUseCases

import com.example.socialapp.domain.repository.PeopleRepository
import com.google.android.gms.tasks.Task
import javax.inject.Inject

class SetUpdatePeoplePostData @Inject constructor(
    private val repository: PeopleRepository
) {

    operator fun invoke(likedBy:  ArrayList<String>?, postId:String): Task<Void> {
        val postObj = mutableMapOf<String, ArrayList<String>?>()
        postObj["likedBy"]= likedBy
        return repository.setUpdatePostData(postObj, postId)

    }    }