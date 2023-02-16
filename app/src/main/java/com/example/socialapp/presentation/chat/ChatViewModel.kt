package com.example.socialapp.presentation.chat

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socialapp.domain.models.ChatList
import com.example.socialapp.domain.models.User
import com.example.socialapp.domain.use_case.chatUseCases.ChatUseCases
import com.example.socialapp.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatUseCases: ChatUseCases
): ViewModel() {
    private var _getChatState = MutableStateFlow<Response<List<ChatList?>>>(Response.Success(emptyList()))
    val getChatState: StateFlow<Response<List<ChatList?>>> = _getChatState
    private var _getReceiverUserState = MutableStateFlow<Response<User?>>(Response.Success(null))
    val getReceiverUserState: StateFlow<Response<User?>> = _getReceiverUserState
    private var _getSenderUserState = MutableStateFlow<Response<User?>>(Response.Success(null))
    val getSenderUserState: StateFlow<Response<User?>> = _getSenderUserState
    private val _setChatState = mutableStateOf<Response<Boolean>>(Response.Success(false))
    val setChatState: State<Response<Boolean>> = _setChatState

    fun getChat(senderId: String) {
        chatUseCases.getALlChats(senderId).onEach {
            when (it) {
                is Response.Loading -> _getChatState.value = Response.Loading
                is Response.Success -> _getChatState.value = Response.Success(it.data)
                is Response.Error -> _getChatState.value = Response.Error(it.message)
            }
        }.launchIn(viewModelScope)
    }
    fun getReceiverUser(userId : String) {
        viewModelScope.launch {
            chatUseCases.getReceiver.invoke(userId).collect{
                _getReceiverUserState.value= it
            }
        }
    }
    fun getSenderUser() {
        viewModelScope.launch {
            chatUseCases.getSender.invoke().collect{
                _getSenderUserState.value= it
            }
        }
    }
    }
