package com.example.socialapp.domain.use_case.MessageUseCases

import com.example.socialapp.domain.repository.MessageRepository
import javax.inject.Inject

class GetMessages @Inject constructor(
    private val repository: MessageRepository
) {
    operator fun invoke(senderRoom: String) =
        repository.getMessages(senderRoom)
    }
