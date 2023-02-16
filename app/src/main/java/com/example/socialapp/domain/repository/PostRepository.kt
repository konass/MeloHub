package com.example.socialapp.domain.repository

import android.net.Uri
import com.example.socialapp.domain.models.Notification
import com.example.socialapp.domain.models.Post
import com.example.socialapp.domain.models.User
import com.example.socialapp.utils.Response
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    suspend fun setPostDataOnDatabase(post: Post): Task<Void>
    fun getPostId() : String
    fun getUserId() : String
   suspend fun uploadPhoto(image: Uri) : String
   fun getAllPosts(): Flow<Response<List<Post>>>
   suspend fun getCurrentUserData(userID: String): User?
    fun updatePost(postObj : Map<String,  ArrayList<String>>, postId: String): Task<Void>
   fun getLike(postId: String) : Flow<Response<List<User>>>
   fun setLike (postId: String, user: User): Flow<Response<Boolean>>
    fun unLike(postId: String, userId: String) :  Flow<Response<Boolean>>
    suspend fun getPostById (postId : String): Post
    fun isLiked(postId: String,user: User ): Flow <Response<Boolean>>
    suspend fun getAuthorLikeById(userId:String) : User
    fun setNotification (receiverId: String, notification: Notification): Flow<Response<Boolean>>
    fun setUpdatePostData(
        postObj : Map<String,  ArrayList<String>?>,
        postId: String
    ): Task<Void>
}