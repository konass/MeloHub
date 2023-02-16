package com.example.socialapp.domain.use_case.MessageUseCases

import com.example.socialapp.domain.models.*
import com.example.socialapp.domain.repository.MessageRepository
import com.example.socialapp.utils.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SaveChat @Inject constructor(
    private val repository : MessageRepository
) {
    private var operationSuccessful: Boolean = false
    operator fun invoke (
   senderId: String,
   message: Message,
   user: User,
   receiverId: String,
    ) : Flow<Response<Boolean>> = flow {
        val chat = ChatList(senderId, message, user)
        operationSuccessful=false
        try {
            emit(Response.Loading)
            repository.saveChat(chat, receiverId, senderId).addOnSuccessListener {
                operationSuccessful = true
            }.await()
            if(operationSuccessful){
                emit(Response.Success(operationSuccessful))
            }
            else{
                emit(Response.Error("Post Upload Unsuccessful"))
            }
        }catch (e: Exception) {
            emit(Response.Error(e.localizedMessage ?: "An Unexpected Error"))
        }
    }

}