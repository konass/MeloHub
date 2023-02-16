package com.example.socialapp.domain.use_case.userUseCases

import com.example.socialapp.domain.repository.UserRepository
import javax.inject.Inject

class GetUserFollowing @Inject constructor(
    private val repository: UserRepository
) {
    operator fun invoke (userId: String) = repository.getFollowing(userId)
}