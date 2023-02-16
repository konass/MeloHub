package com.example.socialapp.domain.use_case.notificationUseCases

import com.example.socialapp.domain.models.Notification
import com.example.socialapp.domain.repository.NotificationRepository
import javax.inject.Inject

class SetNotificationUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    operator fun invoke (receiverId : String, notification: Notification)= repository.setNotification(receiverId,notification)
}
