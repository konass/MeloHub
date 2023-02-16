package com.example.socialapp.domain.use_case.peopleUseCases

import com.example.socialapp.domain.repository.PeopleRepository
import com.google.android.gms.tasks.Task
import javax.inject.Inject

class UpdateFollowing @Inject constructor(
    private val repository : PeopleRepository)
{
    operator fun invoke (following: ArrayList<String>, userId: String) : Task<Void> {
       val userObj =  mutableMapOf<String, ArrayList<String>>()
        userObj["nfollowing"]= following
        return repository.updateFollow(userObj, userId)
    }
}