package com.example.socialapp.domain.use_case.chatUseCases

import com.example.socialapp.domain.repository.ChatRepository
import javax.inject.Inject

class GetALlChats @Inject constructor(
    private val repository: ChatRepository
) {
    operator fun invoke (senderId: String)= repository.getChat(senderId)
}