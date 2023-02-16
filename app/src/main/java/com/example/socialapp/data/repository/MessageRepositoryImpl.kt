package com.example.socialapp.data.repository

import com.example.socialapp.data.firebaseDatabase.Database
import com.example.socialapp.domain.models.ChatList
import com.example.socialapp.domain.models.Message
import com.example.socialapp.domain.models.User
import com.example.socialapp.domain.repository.MessageRepository
import com.example.socialapp.utils.Response
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MessageRepositoryImpl @Inject constructor(
    private val database: Database,
    private val auth: FirebaseAuth
): MessageRepository {
    val userID = auth.currentUser?.uid.toString()
    override fun sentMessage(senderRoom: String, receiverRoom: String, message: Message): Flow<Response<Boolean>> {
        return database.sentMessage(senderRoom,receiverRoom,message)
    }
    override fun getUserId(): String {
        return auth.currentUser?.uid.toString()
    }

    override suspend fun getLatestMessage(senderRoom: String): List<Message?> {
        return database.getLastMessage(senderRoom)
    }
    override fun saveChat(chatList: ChatList,   receiverId: String,  senderId: String): Task<Void> {
        return database.saveChat(chatList,   receiverId, senderId)
    }
    override suspend fun getCurrentUserData(userID: String): User? {
        return database.getCurrentUserData(userID)
    }
    override fun getMessages(senderRoom: String): Flow<Response<List<Message>>> {
        return database.getMessage(senderRoom)
    }
}