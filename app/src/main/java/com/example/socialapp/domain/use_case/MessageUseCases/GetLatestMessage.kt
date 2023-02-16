package com.example.socialapp.domain.use_case.MessageUseCases

import com.example.socialapp.domain.models.Message
import com.example.socialapp.domain.repository.MessageRepository
import com.example.socialapp.utils.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetLatestMessage @Inject constructor(
    private val repository: MessageRepository
){
    operator fun invoke(senderRoom: String): Flow<Response<Message?>> = flow{
        try {
            emit(Response.Loading)
            val  latestMessage = repository.getLatestMessage(senderRoom).last()
            emit(Response.Success(latestMessage))
        }catch (e: Exception) {
            emit(Response.Error(e.localizedMessage ?: " An Unexpected Error"))
        }
    }
}
