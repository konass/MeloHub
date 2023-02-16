package com.example.socialapp.domain.use_case.postUseCases

import android.net.Uri
import com.example.socialapp.domain.repository.PostRepository
import javax.inject.Inject

class UploadImage @Inject constructor(
    private val repository: PostRepository
){
    suspend operator fun invoke(image: Uri) = repository.uploadPhoto(image)
}