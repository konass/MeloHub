package com.example.socialapp.domain.use_case.userUseCases

import android.net.Uri
import com.example.socialapp.domain.repository.UserRepository
import javax.inject.Inject

class UploadUserImage @Inject constructor(
    private val repository: UserRepository
){
    suspend operator fun invoke(image: Uri) = repository.uploadUserImage(image)
}