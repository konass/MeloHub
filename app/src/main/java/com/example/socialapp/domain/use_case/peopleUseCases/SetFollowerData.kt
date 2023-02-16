package com.example.socialapp.domain.use_case.peopleUseCases

import com.example.socialapp.domain.repository.PeopleRepository
import com.example.socialapp.utils.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SetFollowerData @Inject constructor(
    private val repository: PeopleRepository
){
    operator fun invoke( follower: List<Map<String, Boolean>>, userId : String) : Flow<Response<Boolean>> = flow{
        var  operationSuccessful = false
        try{
            val userObj = mutableMapOf<String,List<Map<String, Boolean>>>()
            userObj["folows"] = follower
            repository.setSubscriptionData(userObj, userId)
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