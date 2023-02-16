package com.example.socialapp.domain.use_case.userUseCases

import com.example.socialapp.domain.repository.UserRepository
import com.example.socialapp.utils.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SetUserData @Inject constructor(
    private val repository: UserRepository
){
    operator fun invoke(name: String, lastName: String, imageUrl: String, description: String) : Flow<Response<Boolean>> = flow{
        var  operationSuccessful = false
        try{
            val userObj = mutableMapOf<String,String>()
            val userId= repository.getUserId()
            userObj["name"] = name
            userObj["lastName"] = lastName
            userObj["imageUrl"] = imageUrl
            userObj["description"] = description
            repository.setUserData(userObj, userId)
                .addOnSuccessListener {
                    operationSuccessful = true
                }.await()
            if(operationSuccessful){
                emit(Response.Success(operationSuccessful))
            }
            else{
                emit(Response.Error("Edit Does Not Successful"))
            }
        }
        catch(e:Exception){
            Response.Error(e.localizedMessage?:"An Unexpected Error")
        }
    }
}