package com.example.socialapp.domain.repository

import com.example.socialapp.domain.models.ChatList
import com.example.socialapp.domain.models.User
import com.example.socialapp.utils.Response
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.flow.Flow

interface ChatRepository{
    fun saveChat(chatList: ChatList,   receiverId: String,  senderId: String) : Task<Void>
    fun getChat(senderId: String) : Flow<Response<List<ChatList>>>
    suspend fun getCurrentUserData(userID: String): User?
    fun getUserId(): String
}