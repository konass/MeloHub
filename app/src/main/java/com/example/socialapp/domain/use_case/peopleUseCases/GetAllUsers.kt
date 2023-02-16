package com.example.socialapp.domain.use_case.peopleUseCases

import com.example.socialapp.domain.repository.PeopleRepository
import javax.inject.Inject

class GetAllUsers @Inject constructor(
    private val repository: PeopleRepository
){

    operator fun invoke (userId: String)=repository.getAllUser(userId)
}