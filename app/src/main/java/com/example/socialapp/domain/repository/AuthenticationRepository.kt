package com.example.socialapp.domain.repository

import com.example.socialapp.domain.models.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult


interface AuthenticationRepository {
    fun isUserAuthenticatedInFirebase() : Boolean
    suspend fun getFirebaseAuthState(): Boolean
   suspend fun signIn(email: String, password: String): Task<AuthResult>
   suspend fun signUp(email: String, password: String, name: String, lastName: String): Task<AuthResult>
   suspend fun signOut()
   fun getCurrentUserId():String
    suspend fun setUserDataInfoOnDatabase(user: User)
}