package com.example.socialapp.data.repository

import com.example.socialapp.domain.repository.AuthenticationRepository
import com.example.socialapp.data.firebaseDatabase.Database
import com.example.socialapp.domain.models.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(

    private var database: Database,
    private val auth: FirebaseAuth

) : AuthenticationRepository {
    override fun isUserAuthenticatedInFirebase(): Boolean {
        return auth.currentUser != null
    }

    override suspend fun getFirebaseAuthState(): Boolean {
        return auth.currentUser==null
    }

    override suspend fun signIn(email: String, password: String) : Task<AuthResult> {
return auth.signInWithEmailAndPassword(email, password)
    }

    override suspend fun signUp(
        email: String,
        password: String,
        name: String,
        lastName: String
    ):  Task<AuthResult>{
        return auth.createUserWithEmailAndPassword(email, password)
    }

    override suspend fun signOut() {
      return auth.signOut()

    }

    override fun getCurrentUserId(): String =
        auth.currentUser?.uid.toString()
    override suspend fun setUserDataInfoOnDatabase(user: User) {
        database.setUserDataInfoOnDatabase(user)
    }
}