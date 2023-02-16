package com.example.socialapp.data.firebaseDatabase

import com.example.socialapp.domain.models.Song
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class MusicDatabase @Inject constructor(
    private var db: FirebaseFirestore
) {


    suspend fun getAllSongs(): List<Song> {
        return db.collection("songs").get().await().toObjects(Song::class.java)


}}