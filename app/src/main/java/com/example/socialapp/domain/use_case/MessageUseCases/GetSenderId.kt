package com.example.socialapp.domain.use_case.MessageUseCases

import com.example.socialapp.domain.repository.MessageRepository
import javax.inject.Inject

class GetSenderId @Inject constructor(
    private val repository: MessageRepository
) {
    operator fun invoke () = repository.getUserId()
}