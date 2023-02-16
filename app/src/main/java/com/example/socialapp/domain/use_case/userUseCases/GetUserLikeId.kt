package com.example.socialapp.domain.use_case.userUseCases


import com.example.socialapp.domain.repository.UserRepository
import javax.inject.Inject

class GetUserLikeId @Inject constructor(
    private val repository : UserRepository
) {
    suspend operator fun invoke (userId: String) = repository.getUserLikeById(userId)
}