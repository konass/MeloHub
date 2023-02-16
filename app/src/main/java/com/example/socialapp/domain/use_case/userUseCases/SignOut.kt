package com.example.socialapp.domain.use_case.userUseCases

import com.example.socialapp.domain.repository.UserRepository
import javax.inject.Inject

class SignOut @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke () = repository.signOut()
}