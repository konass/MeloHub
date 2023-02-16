package com.example.socialapp.data.repository

import com.example.socialapp.data.firebaseDatabase.Database
import com.example.socialapp.domain.models.Comment
import com.example.socialapp.domain.models.Post
import com.example.socialapp.domain.models.User
import com.example.socialapp.domain.repository.CommentRepository
import com.example.socialapp.utils.Response
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CommentRepositoryImpl @Inject constructor(
    private val database: Database,
    private val auth: FirebaseAuth
) : CommentRepository {
    override fun getComments(postId: String): Flow<Response<List<Comment>>> {
        return database.getComments(postId)
    }
    override fun setComments(postId: String, comment: Comment): Flow<Response<Boolean>> {
      return database.setComments(postId, comment)
    }
    override fun getUserId(): String {
        return auth.currentUser?.uid.toString()
    }
    override suspend fun getCurrentUserData(userID: String): User? {
        return database.getCurrentUserData(userID)
    }
    override fun updatePost(
        postObj : Map<String,ArrayList<String>>,
        postId: String
    ): Task<Void> {
        return database.updatePost(postObj,postId)
    }
    override suspend fun getPostById(postId: String) : Post {
        return database.getPostById(postId)
    }
}