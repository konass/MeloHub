package com.example.socialapp.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socialapp.domain.models.Followers
import com.example.socialapp.domain.models.Following
import com.example.socialapp.domain.models.Post
import com.example.socialapp.domain.models.User
import com.example.socialapp.domain.use_case.peopleUseCases.PeopleUseCases
import com.example.socialapp.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class ProfilePeopleViewModel @Inject constructor(
    private val peopleUseCases: PeopleUseCases
): ViewModel(){
    private val _setFollowingState = MutableStateFlow<Response<Boolean>>(Response.Success(false))
    val setFollowingState: StateFlow<Response<Boolean>> = _setFollowingState
    private val _setFollowersState = MutableStateFlow<Response<Boolean>>(Response.Success(false))
    val setFollowersState: StateFlow<Response<Boolean>> = _setFollowersState
    private val _getPostState = MutableStateFlow<Response<List<Post?>>>(Response.Success(emptyList()))
    val getPostState: StateFlow<Response<List<Post?>>> = _getPostState
    private var _getUserState = MutableStateFlow<Response<User?>>(Response.Success(null))
    val getUserState: StateFlow<Response<User?>> = _getUserState
    private var _getCurrentUserState = MutableStateFlow<Response<User?>>(Response.Success(null))
    val getCurrentUserState: StateFlow<Response<User?>> = _getCurrentUserState
    private var _getFollowersState = MutableStateFlow<Response<List<Followers>>>(Response.Success(
        emptyList()))
    val getFollowersState : StateFlow<Response<List<Followers>>> = _getFollowersState
    private var _getFollowingState = MutableStateFlow<Response<List<Following>>>(Response.Success(
        emptyList()))
    val getFollowingState : StateFlow<Response<List<Following>>> = _getFollowingState
    private val _unfollowState = MutableStateFlow<Response<Boolean>>(Response.Success(false))
    val unfollowState : StateFlow<Response<Boolean>> = _unfollowState
    fun getUserData(uid: String) {
        viewModelScope.launch {
            peopleUseCases.getUserData.invoke(uid).collect {
                _getUserState.value = it
            }
        }
    }
        fun getPosts(uid: String) {
            peopleUseCases.getPeoplePosts(uid).onEach {
                when (it) {
                    is Response.Loading -> _getPostState.value = Response.Loading
                    is Response.Success -> _getPostState.value = Response.Success(it.data)
                    is Response.Error ->  _getPostState.value = Response.Error(it.message)
                }

            }.launchIn(viewModelScope)
        }
    fun setFollowing (userId:String, childUserId: String, following: Following ) {
        peopleUseCases.setFollowing(userId,childUserId, following).onEach {
            when(it){
                is Response.Loading-> _setFollowingState.value = Response.Loading
                is Response.Success -> _setFollowingState.value = Response.Success(true)
                is Response.Error -> _setFollowingState.value = Response.Error("An unexpected error")
            }
        }.launchIn(viewModelScope)
    }
    fun setFollowers (userId:String, childUserId: String,followers: Followers) {
        peopleUseCases.setFollowers(userId,childUserId, followers).onEach {
            when(it){
                is Response.Loading-> _setFollowingState.value = Response.Loading
                is Response.Success -> _setFollowingState.value = Response.Success(true)
                is Response.Error -> _setFollowingState.value = Response.Error("An unexpected error")
            }
        }.launchIn(viewModelScope)
    }
    fun getUser() {
        viewModelScope.launch {
            peopleUseCases.getCurrentUser.invoke().collect{
                _getCurrentUserState.value= it
            }
        }
    }
    fun getFollowers(userId: String){
        peopleUseCases.getFollowers(userId).onEach{
            when(it){
                is Response.Loading -> _getFollowersState.value = Response.Loading
                is Response.Success -> _getFollowersState.value = Response.Success(it.data)
                is Response.Error ->  _getFollowersState.value = Response.Error(it.message)
            }
        }.launchIn(viewModelScope)
    }
    fun getFollowing(userId: String){
        peopleUseCases.getFollowing(userId).onEach{
            when(it){
                is Response.Loading -> _getFollowingState.value = Response.Loading
                is Response.Success -> _getFollowingState.value = Response.Success(it.data)
                is Response.Error ->  _getFollowingState.value = Response.Error(it.message)
            }
        }.launchIn(viewModelScope)
    }
    fun unfollow(userId:String,childUserId: String ){
       peopleUseCases.unfollow(userId,childUserId).onEach {
           when(it){
               is Response.Loading -> _unfollowState.value = Response.Loading
               is Response.Success -> _unfollowState.value = Response.Success(true)
               is Response.Error ->  _unfollowState.value = Response.Error(it.message)
           }
       }.launchIn(viewModelScope)
    }
    suspend fun isFollow(userId: String, childId: String): Following? {
        return peopleUseCases.isFollow.invoke(userId, childId)
    }
    suspend fun getPostById(postId: String): Post {
        return  peopleUseCases.getPeoplePostById.invoke(postId)

    }
    fun getAuthorLikeId(): String {
        return peopleUseCases.getPeopleUserId.invoke()
    }

    fun updateLike(postId: String) = viewModelScope.launch {
        val currentUserId = getAuthorLikeId()
        val post = getPostById(postId)
        var likesArray = post.likedBy
        val isLiked = post.likedBy?.contains(currentUserId)
        if (isLiked == true){
            likesArray.remove(currentUserId)
            //likesArray=  post.likedBy
        }
        else {
            likesArray.add(currentUserId)
            // likesArray=   post.likedBy
        }
        peopleUseCases.setUpdatePeoplePostData.invoke(likesArray, post.postId!!)
    }
    fun updateFollowers(followers: ArrayList<String>, userId: String){
         peopleUseCases.updateFollowers.invoke(followers, userId)
    }
    fun updateFollowing (following: ArrayList<String>, userId: String){
        peopleUseCases.updateFollowing.invoke(following,userId)
    }
    }

