package com.example.socialapp.data.firebaseStorage

import android.net.Uri
import com.example.socialapp.data.firebaseDatabase.Database
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface Storage {

    suspend fun uploadImage(image: Uri) : String
    suspend fun uploadUserImage(image: Uri, userId: String) : String
}


class StorageByFireBase  @Inject constructor(
    private var storageRef: StorageReference,
    private var database: Database

) : Storage {

    override suspend fun uploadImage(image: Uri): String {
        val imageRef = storageRef.child("images/${database.getPostId()}")
        val result = imageRef.putFile(image).await()
        var uriTask = result.storage.downloadUrl
        while (!uriTask.isSuccessful);
        return uriTask.result.toString()
    }

    override suspend fun uploadUserImage(image: Uri, userId: String): String {
            val imageRef = storageRef.child("images/${userId}")
            val result = imageRef.putFile(image).await()
            var uriTask = result.storage.downloadUrl
            while (!uriTask.isSuccessful);
            return uriTask.result.toString()
    }


}
