package com.example.socialapp.domain.use_case.authenticationUseCases

import com.example.socialapp.domain.repository.AuthenticationRepository
import javax.inject.Inject

class FirebaseAuthState @Inject constructor(
    private val repository: AuthenticationRepository
) {
    suspend operator fun invoke() = repository.getFirebaseAuthState()
}