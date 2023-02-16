package com.example.socialapp.domain.use_case.peopleUseCases

import com.example.socialapp.domain.models.Following
import com.example.socialapp.domain.repository.PeopleRepository
import javax.inject.Inject

class SetFollowing @Inject constructor(
    private val repository: PeopleRepository
) {
    operator fun invoke (userId: String, childUserId : String, following: Following) = repository.setFollowing(userId,childUserId, following )
}