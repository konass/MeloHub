package com.example.socialapp.domain.use_case.authenticationUseCases

data class AuthenticationUseCases(
    val firebaseAuthState: FirebaseAuthState,
    val firebaseIsUserAuthenticated: FirebaseIsUserAuthenticated,
    val firebaseSignIn: FirebaseSignIn,
    val firebaseSignOut: FirebaseSignOut,
    val firebaseSignUp: FirebaseSignUp,
    val getUserId: GetUserId
)
