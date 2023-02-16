package com.example.socialapp.domain.use_case.userUseCases

import com.example.socialapp.domain.repository.UserRepository
import javax.inject.Inject

class GetCurrentUserId@Inject constructor(
    private val repository: UserRepository
) {
   operator fun invoke () = repository.getUserId()
}