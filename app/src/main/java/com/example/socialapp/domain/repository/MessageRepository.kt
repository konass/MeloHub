package com.example.socialapp.domain.repository

import com.example.socialapp.domain.models.ChatList
import com.example.socialapp.domain.models.Message
import com.example.socialapp.domain.models.User
import com.example.socialapp.utils.Response
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.flow.Flow

interface MessageRepository {
    fun sentMessage(senderRoom: String, receiverRoom: String, message : Message): Flow<Response<Boolean>>
    fun getMessages(senderRoom: String): Flow<Response<List<Message>>>
    suspend fun getCurrentUserData(userID: String): User?
    fun getUserId(): String
    suspend fun getLatestMessage(senderRoom: String): List<Message?>
   fun saveChat(chatList: ChatList,   receiverId: String,  senderId: String): Task<Void>
}