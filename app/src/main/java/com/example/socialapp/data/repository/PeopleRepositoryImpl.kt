package com.example.socialapp.data.repository

import com.example.socialapp.data.firebaseDatabase.Database
import com.example.socialapp.domain.models.Followers
import com.example.socialapp.domain.models.Following
import com.example.socialapp.domain.models.Post
import com.example.socialapp.domain.models.User
import com.example.socialapp.domain.repository.PeopleRepository
import com.example.socialapp.utils.Response
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PeopleRepositoryImpl @Inject constructor(
    private val database: Database,
    private val auth: FirebaseAuth,

): PeopleRepository {
    override  fun getAllUser(userId: String): Flow<Response<List<User>>> {
        return database.getAllUsers(userId)
    }
    override suspend fun getUserData(uid: String) : User? {
        return database.getCurrentUserData(uid)
    }
    override fun getUserId(): String {
        return auth.currentUser?.uid.toString()
    }
    override fun getUserPosts(uid:String): Flow<Response<List<Post>>> {
        return database.getUserPosts(uid)
    }
    override fun setSubscriptionData(
        userObj: Map<String, List<Map<String, Boolean>>>,
        userId: String,
    ): Task<Void> {
        return database.setSubscriptionData(userObj,userId)
    }
    override fun setFollowing (userId: String,childUserId: String, following: Following): Flow<Response<Boolean>>{
        return database.setFollowing(userId,childUserId, following)
    }
    override fun setFollowers(userId: String,childUserId: String, followers: Followers): Flow<Response<Boolean>>{
        return database.setFollowers(userId,childUserId, followers)
    }
    override fun getFollowers(userId: String): Flow<Response<List<Followers>>>{
        return database.getFollowers(userId)
    }
    override fun getFollowing(userId: String): Flow<Response<List<Following>>>{
        return database.getFollowing(userId)
    }
    override fun unfollow(userId: String, childUserId:String) :  Flow<Response<Boolean>>{
        return database.unFollow(userId, childUserId)
    }
    override suspend fun isFollow(userId: String, childId: String): Following?{
        return database.isFollow(userId,childId)
    }
    override suspend fun getPeopleLikeById(userId:String) : User {
        return database.getAuthorLikeById(userId)
    }
    override fun setUpdatePostData(
        postObj : Map<String,  ArrayList<String>?>,
        postId: String
    ): Task<Void>{
        return database.setUpdatePostData(postObj, postId)
    }
    override suspend fun getPostById(postId: String) : Post{
        return database.getPostById(postId)
    }
    override fun isLiked(postId: String,user: User ): Flow <Response<Boolean>>{
        return database.isLiked(postId, user)
    }
   override fun updateFollow(
        userObj: Map<String, ArrayList<String>>,
        userId: String
    ): Task<Void>{
       return database.updateFollow(userObj, userId)
   }
    override suspend fun searchUserByName(name: String) : MutableList<User>{
        return database.searchUserByName(name)
    }
    override suspend fun searchUserByLastName(lastName: String) : MutableList<User>{
        return database.searchUserByLastName(lastName)
    }
}