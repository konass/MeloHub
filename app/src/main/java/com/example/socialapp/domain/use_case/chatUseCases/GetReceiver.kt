package com.example.socialapp.domain.use_case.chatUseCases

import com.example.socialapp.domain.models.User
import com.example.socialapp.domain.repository.ChatRepository
import com.example.socialapp.utils.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetReceiver  @Inject constructor(
    private val repository: ChatRepository
) {
    operator fun invoke (userId: String) : Flow<Response<User?>> = flow{
        try {
            emit(Response.Loading)
            val  user = repository.getCurrentUserData(userId)
            emit(Response.Success((user)))
        }catch (e: Exception) {
            emit(Response.Error(e.localizedMessage ?: " An Unexpected Error"))
        }
    }
}