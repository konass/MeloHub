package com.example.socialapp.domain.use_case.userUseCases

import com.example.socialapp.domain.repository.UserRepository
import javax.inject.Inject

class GetUserPosts @Inject constructor(
    private val repository : UserRepository
) {

    operator fun invoke (uid: String)= repository.getUserPosts(uid)
}
