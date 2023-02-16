package com.example.socialapp.data.repository

import android.net.Uri
import com.example.socialapp.data.firebaseDatabase.Database
import com.example.socialapp.data.firebaseStorage.Storage
import com.example.socialapp.domain.models.Followers
import com.example.socialapp.domain.models.Following
import com.example.socialapp.domain.models.Post
import com.example.socialapp.domain.models.User
import com.example.socialapp.domain.repository.UserRepository
import com.example.socialapp.utils.Response
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val database: Database,
    private val auth: FirebaseAuth,
    private val storage: Storage
): UserRepository {
    val userID = auth.currentUser?.uid.toString()
    override suspend fun getCurrentUserData(userID: String): User? {
        return database.getCurrentUserData(userID)
    }

    override suspend fun getUserLikeById(userId:String) : User {
        return database.getAuthorLikeById(userId)
    }
    override fun getUserId(): String {
        return auth.currentUser?.uid.toString()
    }

    override fun setUserData(
        userObj: Map<String, String>,
        userId: String,
    ): Task<Void> {
        return database.setUserData(userObj,userId)
    }

    override suspend fun uploadUserImage(image: Uri): String {
       // val userID = auth.currentUser?.uid.toString()
        return storage.uploadUserImage(image, userID)
    }

    override fun getUserPosts(uid: String): Flow<Response<List<Post>>> {
        return database.getUserPosts(uid)
    }
    override suspend fun signOut() {
        return auth.signOut()
    }
    override fun getFollowers(userId: String): Flow<Response<List<Followers>>>{
        return database.getFollowers(userId)
    }
    override fun getFollowing(userId: String): Flow<Response<List<Following>>>{
        return database.getFollowing(userId)
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
    override fun deletePost(postId: String): Task<Void> {
        return database.deletePost(postId)
    }
}