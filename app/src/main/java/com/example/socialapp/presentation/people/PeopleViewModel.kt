package com.example.socialapp.presentation.people

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class PeopleViewModel @Inject constructor(
    private val peopleUseCase: PeopleUseCases
): ViewModel(){
    private val _getUserState = MutableStateFlow<Response<List<User?>>>(Response.Success(emptyList()))
    val getUserState: StateFlow<Response<List<User?>>> = _getUserState
    private var _getCurrentUserState = MutableStateFlow<Response<User?>>(Response.Success(null))
    val getCurrentUserState: StateFlow<Response<User?>> = _getCurrentUserState
        fun getAllUser(userId: String) {
        peopleUseCase.getAllUsers(userId).onEach {
            when (it) {
                is Response.Loading -> _getUserState.value = Response.Loading
                is Response.Success -> _getUserState.value = Response.Success(it.data)
                is Response.Error ->  _getUserState.value = Response.Error(it.message)
            }

        }.launchIn(viewModelScope)
    }
    suspend fun searchUserByName(name: String): MutableList<User> {
        return peopleUseCase.searchUserByName.invoke(name)
    }
    suspend fun searchUserByLastName(lastName: String): MutableList<User> {
        return peopleUseCase.searchUserByLastName.invoke(lastName)
    }
    fun getCurrentUser() {
        viewModelScope.launch {
            peopleUseCase.getCurrentUser.invoke().collect{
                _getCurrentUserState.value= it
            }
        }
    }
}