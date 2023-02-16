package com.example.socialapp.domain.use_case.MessageUseCases


import com.example.socialapp.domain.models.Message
import com.example.socialapp.domain.repository.MessageRepository
import javax.inject.Inject

class SentMessage  @Inject constructor(
    private val repository: MessageRepository
) {

    operator fun invoke(senderRoom: String, receiverRoom: String, message : Message) = repository.sentMessage(senderRoom, receiverRoom,message)


}