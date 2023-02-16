package com.example.socialapp.domain.use_case.authenticationUseCases

import com.example.socialapp.domain.repository.AuthenticationRepository
import javax.inject.Inject

class GetUserId @Inject constructor(
    private val repository: AuthenticationRepository
){
    operator fun invoke()= repository.getCurrentUserId()
}