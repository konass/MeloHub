package com.example.socialapp.data.repository

import android.net.Uri
import com.example.socialapp.domain.repository.PostRepository
import com.example.socialapp.data.firebaseDatabase.Database
import com.example.socialapp.data.firebaseStorage.Storage
import com.example.socialapp.domain.models.Notification
import com.example.socialapp.domain.models.Post
import com.example.socialapp.domain.models.User
import com.example.socialapp.utils.Response
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val database: Database,
    private val auth: FirebaseAuth,
    private val storage: Storage
): PostRepository {



    override suspend fun setPostDataOnDatabase(post: Post): Task<Void> {
      return  database.setPostDataInfoOnDatabase(post)
    }
    override fun getPostId(): String {
        return database.getPostId()
    }

    override fun getUserId(): String {
        return auth.currentUser?.uid.toString()
    }
    override suspend fun getCurrentUserData(userID: String): User? {
        return database.getCurrentUserData(userID)
    }
    override suspend fun uploadPhoto(image: Uri): String {
        return storage.uploadImage(image)
    }

    override fun getAllPosts(): Flow<Response<List<Post>>> {
      return  database.getAllPosts()
    }
    override fun updatePost(
        postObj : Map<String, ArrayList<String>>,
        postId: String
    ): Task<Void>{
        return database.updatePost(postObj,postId)
    }
    override fun setLike (postId: String, user: User): Flow<Response<Boolean>>{
        return database.setLike(postId, user)
    }
    override fun getLike(postId: String) : Flow<Response<List<User>>>{
        return database.getLike(postId)
    }
    override fun unLike(postId: String, userId: String) :  Flow<Response<Boolean>>{
        return database.unLike(postId, userId)
    }
    override suspend fun getPostById(postId: String) : Post{
        return database.getPostById(postId)
    }
    override fun isLiked(postId: String,user: User ): Flow <Response<Boolean>>{
        return database.isLiked(postId, user)
    }
    override suspend fun getAuthorLikeById(userId:String) : User {
        return database.getAuthorLikeById(userId)
    }
    override fun setUpdatePostData(
        postObj : Map<String,  ArrayList<String>?>,
        postId: String
    ): Task<Void>{
        return database.setUpdatePostData(postObj, postId)
    }
    override fun setNotification(
        receiverId: String,
        notification: Notification,
    ): Flow<Response<Boolean>> {
        return database.setNotification(receiverId, notification)
    }
}