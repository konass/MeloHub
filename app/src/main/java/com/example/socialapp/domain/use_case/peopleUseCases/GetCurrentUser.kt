package com.example.socialapp.domain.use_case.peopleUseCases

import com.example.socialapp.domain.models.User
import com.example.socialapp.domain.repository.PeopleRepository
import com.example.socialapp.utils.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCurrentUser @Inject constructor(
    private val repository: PeopleRepository
) {
    operator fun invoke (): Flow<Response<User?>> = flow{
        try {
            emit(Response.Loading)
            val userID = repository.getUserId()
            val  user = repository.getUserData(userID)
            emit(Response.Success((user)))
        }catch (e: Exception) {
            emit(Response.Error(e.localizedMessage ?: " An Unexpected Error"))
        }
    }
}