package com.example.socialapp.domain.repository

import com.example.socialapp.domain.models.Followers
import com.example.socialapp.domain.models.Following
import com.example.socialapp.domain.models.Post
import com.example.socialapp.domain.models.User
import com.example.socialapp.utils.Response
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.flow.Flow

interface PeopleRepository {
     fun getAllUser(userId: String): Flow<Response<List<User>>>
    suspend fun getUserData(uid: String): User?
    fun getUserPosts(uid: String): Flow<Response<List<Post>>>
    fun setSubscriptionData(
        userObj: Map<String, List<Map<String, Boolean>>>,
        userId: String,
    ): Task<Void>
    fun setFollowers(userId: String, childUserId: String,followers: Followers): Flow<Response<Boolean>>
    fun setFollowing(userId: String, childUserId : String, following: Following): Flow<Response<Boolean>>
    fun getUserId(): String
    fun getFollowers(userId: String): Flow<Response<List<Followers>>>
    fun getFollowing(userId: String): Flow<Response<List<Following>>>
   fun unfollow(userId: String, childUserId:String) :  Flow<Response<Boolean>>
    suspend fun isFollow(userId: String, childId: String):Following?
    suspend fun getPeopleLikeById(userId:String) : User
    suspend fun getPostById (postId : String): Post
    fun isLiked(postId: String,user: User ): Flow <Response<Boolean>>
    fun setUpdatePostData(
        postObj : Map<String,  ArrayList<String>?>,
        postId: String
    ): Task<Void>
    fun updateFollow(
        userObj: Map<String, ArrayList<String>>,
        userId: String
    ): Task<Void>
    suspend fun searchUserByName(name: String) : MutableList<User>
    suspend fun searchUserByLastName(name: String) : MutableList<User>
}