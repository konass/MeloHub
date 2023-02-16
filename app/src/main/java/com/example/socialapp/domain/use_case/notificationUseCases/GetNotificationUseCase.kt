package com.example.socialapp.domain.use_case.notificationUseCases

import com.example.socialapp.domain.repository.NotificationRepository
import javax.inject.Inject

class GetNotificationUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    operator fun invoke (receiverId : String) = repository.getNotification(receiverId)
}