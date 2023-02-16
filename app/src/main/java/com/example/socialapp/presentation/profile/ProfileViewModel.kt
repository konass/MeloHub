package com.example.socialapp.presentation.profile

import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socialapp.domain.models.Followers
import com.example.socialapp.domain.models.Following
import com.example.socialapp.domain.models.Post
import com.example.socialapp.domain.models.User
import com.example.socialapp.domain.use_case.userUseCases.UserUseCases
import com.example.socialapp.utils.Response

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userUseCases: UserUseCases
): ViewModel() {
    private var _getUserState = MutableStateFlow<Response<User?>>(Response.Success(null))
    val getUserState: StateFlow<Response<User?>> = _getUserState
    private val _updateUserState = mutableStateOf<Response<Boolean>>(Response.Success(false))
    val updateUserState: State<Response<Boolean>> = _updateUserState
    private val _getPostState = MutableStateFlow<Response<List<Post?>>>(Response.Success(emptyList()))
    val getPostState: StateFlow<Response<List<Post?>>> = _getPostState
    private var _getFollowersState = MutableStateFlow<Response<List<Followers>>>(Response.Success(
        emptyList()))
    val getFollowersState : StateFlow<Response<List<Followers>>> = _getFollowersState
    private var _getFollowingState = MutableStateFlow<Response<List<Following>>>(Response.Success(
        emptyList()))
    val getFollowingState : StateFlow<Response<List<Following>>> = _getFollowingState
    fun getPosts(uid: String) {
        userUseCases.getUserPosts(uid).onEach {
            when (it) {
                is Response.Loading -> _getPostState.value = Response.Loading
                is Response.Success -> _getPostState.value = Response.Success(it.data)
                is Response.Error ->  _getPostState.value = Response.Error(it.message)
            }

        }.launchIn(viewModelScope)
    }
    fun getCurrentUser() {
        viewModelScope.launch {
            userUseCases.getCurrentUserData.invoke().collect{
                _getUserState.value= it
            }
        }
    }
    fun updateUser(name: String, lastName: String, imageUrl: String, description: String) {
        userUseCases.setUserData(name, lastName, imageUrl, description).onEach {
            when(it){
                is Response.Loading-> _updateUserState.value = Response.Loading
                is Response.Success -> _updateUserState.value = Response.Success(true)
                is Response.Error -> _updateUserState.value = Response.Error("An unexpected error")
            }
        }.launchIn(viewModelScope)

    }
    fun uploadUserImage(image: Uri){
        viewModelScope.launch {
            userUseCases.uploadUserImage.invoke(image)
        }
    }
    fun signOut () {
        viewModelScope.launch {
            userUseCases.signOut.invoke()
        }
    }

    fun getCurrentUserId(){
        viewModelScope.launch {
            userUseCases.getUserId
        }
    }
    fun getAuthorLikeId(): String {
        return userUseCases.getUserId.invoke()
    }
    fun deletePost(postId: String) = viewModelScope.launch {
         userUseCases.deletePostUseCase.invoke(postId)
    }
    fun getFollowers(userId: String){
        userUseCases.getUserFollowers(userId).onEach{
            when(it){
                is Response.Loading -> _getFollowersState.value = Response.Loading
                is Response.Success -> _getFollowersState.value = Response.Success(it.data)
                is Response.Error ->  _getFollowersState.value = Response.Error(it.message)
            }
        }.launchIn(viewModelScope)
    }
    fun getFollowing(userId: String){
        userUseCases.getUserFollowing(userId).onEach{
            when(it){
                is Response.Loading -> _getFollowingState.value = Response.Loading
                is Response.Success -> _getFollowingState.value = Response.Success(it.data)
                is Response.Error ->  _getFollowingState.value = Response.Error(it.message)
            }
        }.launchIn(viewModelScope)
    }
    suspend fun getPostById(postId: String): Post {
        return  userUseCases.getUserPostById.invoke(postId)

    }
    fun updateLike(postId: String) = viewModelScope.launch {
        val currentUserId = getAuthorLikeId()
        val post = getPostById(postId)
        var likesArray = post.likedBy
        val isLiked = post.likedBy?.contains(currentUserId)
        if (isLiked == true){
            likesArray.remove(currentUserId)

        }
        else {
            likesArray.add(currentUserId)

        }
        userUseCases.setUserUpdatePostData.invoke(likesArray, post.postId!!)
    }
}