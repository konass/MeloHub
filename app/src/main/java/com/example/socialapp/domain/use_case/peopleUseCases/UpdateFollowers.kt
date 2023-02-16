package com.example.socialapp.domain.use_case.peopleUseCases

import com.example.socialapp.domain.repository.PeopleRepository
import com.google.android.gms.tasks.Task
import javax.inject.Inject

class UpdateFollowers @Inject constructor(
    private val repository : PeopleRepository
)
{
    operator fun invoke (followers: ArrayList<String>, userId: String) : Task<Void> {
        val userObj =  mutableMapOf<String, ArrayList<String>>()
        userObj["nfollowers"]= followers
        return repository.updateFollow(userObj, userId)
    }
}