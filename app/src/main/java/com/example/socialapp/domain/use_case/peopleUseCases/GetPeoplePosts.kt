package com.example.socialapp.domain.use_case.peopleUseCases

import com.example.socialapp.domain.repository.PeopleRepository

import javax.inject.Inject

class GetPeoplePosts @Inject constructor(
        private val repository : PeopleRepository
    ) {
        operator fun invoke (uid: String)= repository.getUserPosts(uid)
    }

