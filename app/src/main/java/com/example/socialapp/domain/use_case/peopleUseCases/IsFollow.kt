package com.example.socialapp.domain.use_case.peopleUseCases

import com.example.socialapp.domain.repository.PeopleRepository
import javax.inject.Inject

class IsFollow @Inject constructor(
    private val repository: PeopleRepository
) {
 suspend operator fun invoke (userId: String, childId: String)= repository.isFollow(userId,childId)
}