package com.example.socialapp.domain.use_case.peopleUseCases

import com.example.socialapp.domain.models.User
import com.example.socialapp.domain.repository.PeopleRepository
import com.example.socialapp.utils.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetUserData @Inject constructor(
    private val repository: PeopleRepository
) {
    operator fun invoke (uid: String): Flow<Response<User?>> = flow{
        try {
            emit(Response.Loading)
            val  user = repository.getUserData(uid)
            emit(Response.Success((user)))
        }catch (e: Exception) {
            emit(Response.Error(e.localizedMessage ?: " An Unexpected Error"))
        }
    }
}