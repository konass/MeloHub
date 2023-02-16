package com.example.socialapp.domain.use_case.authenticationUseCases

import com.example.socialapp.domain.repository.AuthenticationRepository
import com.example.socialapp.utils.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FirebaseSignOut @Inject constructor(
    private val repository: AuthenticationRepository
) {
    operator fun invoke (): Flow<Response<Boolean>> = flow {
        try {
            emit(Response.Loading)
            repository.signOut()
            emit(Response.Success(true))
        } catch (e: Exception) {
            emit(Response.Error(e.localizedMessage ?: "An Unexpected Error"))
        }
    }
}