package com.example.socialapp.domain.use_case.peopleUseCases

import com.example.socialapp.domain.repository.PeopleRepository
import javax.inject.Inject

class GetPeopleLikeId @Inject constructor(
    private val repository : PeopleRepository
) {
    suspend operator fun invoke (userId: String) = repository.getPeopleLikeById(userId)
}