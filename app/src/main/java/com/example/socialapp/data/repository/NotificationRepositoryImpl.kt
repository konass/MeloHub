package com.example.socialapp.data.repository

import com.example.socialapp.data.firebaseDatabase.Database
import com.example.socialapp.domain.models.Notification
import com.example.socialapp.domain.repository.NotificationRepository
import com.example.socialapp.utils.Response
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    val db: Database
): NotificationRepository {
    override fun getNotification(
        receiverId: String
    ): Flow<Response<List<Notification>>> {
        return db.getNotification(receiverId)
    }

    override fun setNotification(
        receiverId: String,
        notification: Notification,
    ): Flow<Response<Boolean>> {
        return db.setNotification(receiverId, notification)
    }

}