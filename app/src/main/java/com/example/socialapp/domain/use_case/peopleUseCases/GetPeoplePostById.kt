package com.example.socialapp.domain.use_case.peopleUseCases

import com.example.socialapp.domain.repository.PeopleRepository
import javax.inject.Inject

class GetPeoplePostById @Inject constructor(
    private val repository: PeopleRepository
) {
    suspend operator fun invoke (postId: String) = repository.getPostById(postId)
}
