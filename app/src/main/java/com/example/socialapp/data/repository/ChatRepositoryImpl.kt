package com.example.socialapp.data.repository

import com.example.socialapp.data.firebaseDatabase.Database
import com.example.socialapp.domain.models.ChatList
import com.example.socialapp.domain.models.User
import com.example.socialapp.domain.repository.ChatRepository
import com.example.socialapp.utils.Response
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val database: Database,
    private val auth: FirebaseAuth
):ChatRepository {
    override fun saveChat(chatList: ChatList,   receiverId: String, senderId: String): Task<Void> {
        return database.saveChat(chatList,   receiverId, senderId)
    }

    override fun getChat(senderId: String): Flow<Response<List<ChatList>>> {
        return database.getAllChat(senderId)
    }
    override fun getUserId(): String {
        return auth.currentUser?.uid.toString()
    }
    override suspend fun getCurrentUserData(userID: String): User? {
        return database.getCurrentUserData(userID)
    }

}