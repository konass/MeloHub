package com.example.socialapp.domain.use_case.peopleUseCases

import com.example.socialapp.domain.models.Followers
import com.example.socialapp.domain.repository.PeopleRepository
import javax.inject.Inject

class SetFollowers @Inject constructor(
    private val repository: PeopleRepository
) {
    operator fun invoke (userId: String, childUserId: String,followers: Followers) = repository.setFollowers(userId,childUserId, followers)
}