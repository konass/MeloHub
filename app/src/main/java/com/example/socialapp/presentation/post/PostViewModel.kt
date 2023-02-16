package com.example.socialapp.presentation.post

import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socialapp.domain.models.Music
import com.example.socialapp.domain.models.User
import com.example.socialapp.domain.use_case.postUseCases.PostUseCases
import com.example.socialapp.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val postUseCases: PostUseCases
): ViewModel() {
    private val _setPostState = mutableStateOf<Response<Boolean>>(Response.Success(false))
    val setPostState: State<Response<Boolean>> = _setPostState
    private var _getUserState = MutableStateFlow<Response<User?>>(Response.Success(null))
    val getUserState: StateFlow<Response<User?>> = _getUserState
    fun setPostData (
        imageUrl: String,
        author: User?,
        postText: String,
        time: Long?,
        nLikes: Int?,
        likedBy: ArrayList<String>,
        likes: List<User>?,
        comments: ArrayList<String>,
        music: Music?
    ){
        postUseCases.setPostDataOnDatabase(
            imageUrl = imageUrl,
            author=author,
            postText=postText,
            time=time,
            nLikes=nLikes,
            likedBy= likedBy,
            likes=likes,
           comments= comments,
            music=music).onEach {
            when(it){
                is Response.Loading-> _setPostState.value = Response.Loading
                is Response.Success -> _setPostState.value = Response.Success(true)
                is Response.Error -> _setPostState.value = Response.Error(it.message)
            }
        }.launchIn(viewModelScope)
    }
    fun uploadImage(image: Uri){
        viewModelScope.launch {
            postUseCases.uploadImage.invoke(image)
        }
    }
    fun getUser() {
        viewModelScope.launch {
            postUseCases.getPostAuthor.invoke().collect{
                _getUserState.value= it
            }
        }
    }
}
