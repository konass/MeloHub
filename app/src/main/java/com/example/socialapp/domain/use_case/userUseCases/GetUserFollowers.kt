package com.example.socialapp.domain.use_case.userUseCases


import com.example.socialapp.domain.repository.UserRepository
import javax.inject.Inject

class GetUserFollowers @Inject constructor(
    private val repository: UserRepository
) {
    operator fun invoke (userId: String) = repository.getFollowers(userId)
}