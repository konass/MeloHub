package com.example.socialapp.domain.repository

import android.net.Uri
import com.example.socialapp.domain.models.Followers
import com.example.socialapp.domain.models.Following
import com.example.socialapp.domain.models.Post
import com.example.socialapp.domain.models.User
import com.example.socialapp.utils.Response
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.flow.Flow

interface UserRepository{
    suspend fun getCurrentUserData(userID:String): User?
    fun getUserId(): String
    fun setUserData(userObj : Map<String,String>, userId: String): Task<Void>
    suspend fun uploadUserImage(image: Uri): String
    fun   getUserPosts(uid: String): Flow<Response<List<Post>>>
    suspend fun signOut()
    fun getFollowers(userId: String): Flow<Response<List<Followers>>>
    fun getFollowing(userId: String): Flow<Response<List<Following>>>
    suspend fun getUserLikeById(userId:String) : User
    suspend fun getPostById (postId : String): Post
    fun isLiked(postId: String,user: User ): Flow <Response<Boolean>>
    fun setUpdatePostData(
        postObj : Map<String,  ArrayList<String>?>,
        postId: String
    ): Task<Void>
    fun deletePost(postId: String): Task<Void>

}