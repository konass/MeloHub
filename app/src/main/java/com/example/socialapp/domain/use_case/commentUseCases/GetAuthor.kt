package com.example.socialapp.domain.use_case.commentUseCases

import com.example.socialapp.domain.models.User
import com.example.socialapp.domain.repository.CommentRepository
import com.example.socialapp.utils.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetAuthor @Inject constructor(
    private val repository : CommentRepository
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