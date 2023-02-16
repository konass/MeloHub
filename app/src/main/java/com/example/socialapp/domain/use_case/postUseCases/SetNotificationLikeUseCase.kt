package com.example.socialapp.domain.use_case.postUseCases


import com.example.socialapp.domain.models.Notification
import com.example.socialapp.domain.repository.PostRepository
import javax.inject.Inject

class SetNotificationLikeUseCase @Inject constructor(
    private val repository: PostRepository
) {
    operator fun invoke ( receiverId : String, notification: Notification)= repository.setNotification(receiverId,notification)
}
