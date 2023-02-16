package com.example.socialapp.domain.use_case.postUseCases

import com.example.socialapp.domain.repository.PostRepository
import com.example.socialapp.utils.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UpdateLike @Inject constructor(
    private val repository: PostRepository
) {

}