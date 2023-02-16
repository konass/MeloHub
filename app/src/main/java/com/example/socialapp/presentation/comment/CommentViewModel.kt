package com.example.socialapp.presentation.comment

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socialapp.domain.models.Comment
import com.example.socialapp.domain.models.User
import com.example.socialapp.domain.use_case.commentUseCases.CommentUseCases
import com.example.socialapp.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class CommentViewModel @Inject constructor(
    private val commentUseCases : CommentUseCases
): ViewModel() {
    private var _getCommentState = MutableStateFlow<Response<List<Comment?>>>(Response.Success(emptyList()))
    val getCommentState: StateFlow<Response<List<Comment?>>> = _getCommentState
    private var  _setCommentState= MutableStateFlow<Response<Boolean>>(Response.Success(false))
    val setCommentState : StateFlow<Response<Boolean>> = _setCommentState
    private var _getUserState = MutableStateFlow<Response<User?>>(Response.Success(null))
    val getUserState: StateFlow<Response<User?>> = _getUserState
    private val _updateCommentState = mutableStateOf<Response<Boolean>>(Response.Success(false))
    val updateCommentState: State<Response<Boolean>> = _updateCommentState
    fun getComment(postId: String){
        commentUseCases.getComments(postId).onEach{
            when (it) {
                is Response.Loading -> _getCommentState.value = Response.Loading
                is Response.Success -> _getCommentState.value = Response.Success(it.data)
                is Response.Error -> _getCommentState.value = Response.Error(it.message)
            }
        }.launchIn(viewModelScope)
    }
    fun setComment(postId: String, comment: Comment) {
        commentUseCases.setComment(postId, comment).onEach {
            when (it) {
                is Response.Loading -> _setCommentState.value = Response.Loading
                is Response.Success -> _setCommentState.value = Response.Success(false)
                is Response.Error -> _setCommentState.value = Response.Error(it.message)
            }
        }.launchIn(viewModelScope)
    }
    fun getUser() {
        viewModelScope.launch {
            commentUseCases.getAuthor.invoke().collect{
                _getUserState.value= it
            }
        }
    }
    fun updateComment(postId: String, userId: String)=viewModelScope.launch {
        val post = commentUseCases.getPostForCommentById.invoke(postId)
        post.comments.add(userId)
        commentUseCases.updateComment(post.comments, postId)
    }


}