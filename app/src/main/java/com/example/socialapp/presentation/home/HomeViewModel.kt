package com.example.socialapp.presentation.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socialapp.domain.models.Notification
import com.example.socialapp.domain.models.Post
import com.example.socialapp.domain.models.User
import com.example.socialapp.domain.use_case.postUseCases.PostUseCases
import com.example.socialapp.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class HomeViewModel@Inject constructor(
    private val postUseCases: PostUseCases
): ViewModel()  {
    private val _getPostState = MutableStateFlow<Response<List<Post?>>>(Response.Success(emptyList()))
    val getPostState: StateFlow<Response<List<Post?>>> = _getPostState
    private var _getUserState = MutableStateFlow<Response<User?>>(Response.Success(null))
    val getUserState: StateFlow<Response<User?>> = _getUserState
    private var _setNotificationState = MutableStateFlow<Response<Boolean>>(Response.Success(false))
    val setNotificationState : StateFlow<Response<Boolean>> = _setNotificationState
    var isLikedFlag = false
    fun getPosts() {
        postUseCases.getPostsUseCase().onEach {
            when (it) {
                is Response.Loading -> _getPostState.value = Response.Loading
                is Response.Success -> _getPostState.value = Response.Success(it.data)
                is Response.Error ->  _getPostState.value = Response.Error(it.message)
            }

        }.launchIn(viewModelScope)
    }
    suspend fun getCurrentUser(userId: String): User? {
        return postUseCases.getCurrentUserUseCase.invoke(userId)
    }
    suspend fun getPostById(postId: String): Post {
          return  postUseCases.getPostById.invoke(postId)

        }
   suspend fun getAuthorLikeById(userId: String): User {
           return postUseCases.getAuthorLikeById.invoke(userId)

    }
    fun getAuthorLikeId(): String {
        return postUseCases.getAuthorLikeId.invoke()
    }
    fun setNotification(receiverId: String, notification: Notification){
        postUseCases.setNotificationLikeUseCase(receiverId, notification).onEach{
            when(it){
                is Response.Loading -> _setNotificationState.value = Response.Loading
                is Response.Success -> _setNotificationState.value = Response.Success(it.data)
                is Response.Error -> _setNotificationState.value  = Response.Error(it.message)
            }
        }.launchIn(viewModelScope)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun updateLike(postId: String) = viewModelScope.launch {
        val currentUserId = getAuthorLikeId()
        val currentUser = getCurrentUser(currentUserId)
        val post = getPostById(postId)
        var likesArray = post.likedBy
        val isLiked = post.likedBy?.contains(currentUserId)
        if (isLiked == true){
            isLikedFlag =false
            likesArray.remove(currentUserId)
        }
        else {
            isLikedFlag =true
           likesArray.add(currentUserId)
            val notification = Notification ("${currentUser?.name} ${currentUser?.lastName}  liked your post at ${LocalDateTime.now().format(DateTimeFormatter.ofPattern("H:m:ss"))}")
            setNotification(post.userId!!, notification )
        }
        postUseCases.setUpdatePostData.invoke(likesArray, post.postId!!)
    }
}