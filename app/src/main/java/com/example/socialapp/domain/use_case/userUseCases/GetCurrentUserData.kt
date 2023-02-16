package com.example.socialapp.domain.use_case.userUseCases

import com.example.socialapp.domain.models.User
import com.example.socialapp.domain.repository.UserRepository
import com.example.socialapp.utils.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCurrentUserData @Inject constructor(
    private val repository: UserRepository
) {

    operator fun invoke (): Flow<Response<User?>> = flow{
        try {
            emit(Response.Loading)
            val userID = repository.getUserId()
            val  user = repository.getCurrentUserData(userID)
            emit(Response.Success((user)))
        }catch (e: Exception) {
            emit(Response.Error(e.localizedMessage ?: " An Unexpected Error"))
        }
    }
}