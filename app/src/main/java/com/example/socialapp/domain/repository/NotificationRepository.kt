package com.example.socialapp.domain.repository

import com.example.socialapp.domain.models.Notification
import com.example.socialapp.utils.Response
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {
    fun getNotification(receiverId: String) : Flow<Response<List<Notification>>>
    fun setNotification (receiverId: String, notification: Notification): Flow<Response<Boolean>>
}