package com.example.socialapp.presentation.notifications


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socialapp.domain.models.Notification
import com.example.socialapp.domain.use_case.notificationUseCases.NotificationUseCases
import com.example.socialapp.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val notificationUseCase: NotificationUseCases
): ViewModel() {
private var _setNotificationState = MutableStateFlow<Response<Boolean>>(Response.Success(false))
 val setNotificationState : StateFlow<Response<Boolean>> = _setNotificationState
    private var _getNotificationState = MutableStateFlow<Response<List<Notification>>>(Response.Success(emptyList()))
    val getNotificationState : StateFlow<Response<List<Notification>>> = _getNotificationState
 fun setNotification(receiverId: String, notification: Notification){
     notificationUseCase.setNotificationUseCase(receiverId,notification).onEach{
         when(it){
             is Response.Loading -> _setNotificationState.value = Response.Loading
             is Response.Success -> _setNotificationState.value = Response.Success(it.data)
             is Response.Error -> _setNotificationState.value  = Response.Error(it.message)
         }
     }.launchIn(viewModelScope)
 }
    fun getNotification (receiverId: String) {
        notificationUseCase.getNotificationUseCase(receiverId).onEach {
            when(it) {
                is Response.Loading -> _getNotificationState.value = Response.Loading
                is Response.Success -> _getNotificationState.value = Response.Success(it.data)
                is Response.Error -> _getNotificationState.value = Response.Error(it.message)
            }
        }.launchIn(viewModelScope)
    }
}