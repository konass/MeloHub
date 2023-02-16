package com.example.socialapp.presentation.chat

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socialapp.domain.models.Message
import com.example.socialapp.domain.models.User
import com.example.socialapp.domain.use_case.MessageUseCases.MessageUseCases
import com.example.socialapp.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MessagingViewModel @Inject constructor (
    private val messageUseCases: MessageUseCases) : ViewModel(){
    private val _setChatState = mutableStateOf<Response<Boolean>>(Response.Success(false))
    val setChatState: State<Response<Boolean>> = _setChatState
    private var _getUserState = MutableStateFlow<Response<User?>>(Response.Success(null))
    val getUserState: StateFlow<Response<User?>> = _getUserState
    private val _sentMessageState = MutableStateFlow<Response<Boolean>>(Response.Success(false))
    val setMessageState: StateFlow<Response<Boolean>> = _sentMessageState
    private val _getMessagesState = MutableStateFlow<Response<List<Message>>>(Response.Success(emptyList()))
    val getMessagesState: StateFlow<Response<List<Message>>> = _getMessagesState
    fun saveChat(senderId: String, message: Message, user: User,   receiverId: String){
        messageUseCases.saveChat(senderId, message, user, receiverId).onEach {
            when (it){
                is Response.Loading -> _setChatState.value = Response.Loading
                is Response.Success -> _setChatState.value = Response.Success(true)
                is Response.Error-> _setChatState.value = Response.Error(it.message)
            }
        }.launchIn(viewModelScope)
    }
    fun sentMessage(senderRoom: String, receiverRoom: String, message : Message) {
        messageUseCases.sentMessage(senderRoom,receiverRoom, message).onEach {
            when (it) {
                is Response.Loading -> _sentMessageState.value = Response.Loading
                is Response.Success -> _sentMessageState.value = Response.Success(true)
                is Response.Error -> _sentMessageState.value = Response.Error(it.message)
            }

        }.launchIn(viewModelScope)
    }

    fun getMessages(senderRoom: String, ) {
        messageUseCases.getMessages(senderRoom).onEach {
            when (it) {
                is Response.Loading -> _getMessagesState.value = Response.Loading
                is Response.Success -> _getMessagesState.value = Response.Success(it.data)
                is Response.Error -> _getMessagesState.value = Response.Error(it.message)
            }

        }.launchIn(viewModelScope)
    }
    fun getUser() {
        viewModelScope.launch {
            messageUseCases.getUser.invoke().collect{
                _getUserState.value= it
            }
        }
    }
}