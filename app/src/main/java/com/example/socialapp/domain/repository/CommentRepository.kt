package com.example.socialapp.domain.repository

import com.example.socialapp.domain.models.Comment
import com.example.socialapp.domain.models.Post
import com.example.socialapp.domain.models.User
import com.example.socialapp.utils.Response
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.flow.Flow

interface CommentRepository {
    fun getComments(postId: String) : Flow<Response<List<Comment>>>
    fun setComments (postId: String, comment: Comment): Flow<Response<Boolean>>
    fun getUserId(): String
    suspend fun getCurrentUserData(userID: String): User?
    fun updatePost(
        postObj : Map<String, ArrayList<String>>,
        postId: String
    ): Task<Void>
    suspend fun getPostById(postId: String) : Post
}