package com.example.socialapp.domain.use_case.peopleUseCases

import com.example.socialapp.domain.repository.PeopleRepository
import javax.inject.Inject

class GetPeopleUserId@Inject constructor(
    private val repository: PeopleRepository
) {
   operator fun invoke () = repository.getUserId()
}