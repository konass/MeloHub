package com.example.socialapp.domain.use_case.authenticationUseCases

import com.example.socialapp.domain.models.User
import com.example.socialapp.domain.repository.AuthenticationRepository
import com.example.socialapp.utils.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseSignUp @Inject constructor(
    private val repository: AuthenticationRepository
){
    var operationSuccessful: Boolean = false
    operator fun invoke (email: String, password: String, name: String, lastName: String): Flow<Response<Boolean>> = flow{
        operationSuccessful=false
        try {
            emit(Response.Loading)
            repository.signUp(email, password, name, lastName).addOnSuccessListener {
                operationSuccessful = true
            }.await()
            val userId = repository.getCurrentUserId()
            var user= User(
                userId=userId!!,
                password=password,
                email=email,
                name= name,
                lastName= lastName,
                nickName= "",
                imageUrl= "",
                description = "",
                nfollowers=ArrayList(),
                nfollowing=ArrayList()
            )
                repository.setUserDataInfoOnDatabase(user)

            emit(Response.Success(operationSuccessful))
        }catch (e: Exception) {
            emit(Response.Error(e.localizedMessage ?: "An Unexpected Error"))
        }
    }
}