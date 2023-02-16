package com.example.socialapp.data.firebaseDatabase

import android.util.Log
import com.example.socialapp.domain.models.*
import com.example.socialapp.utils.Response
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface Database {

    suspend fun setUserDataInfoOnDatabase(user: User)
    suspend fun setPostDataInfoOnDatabase(post:Post): Task<Void>
    suspend fun getCurrentUserData(id: String): User?
    fun getAllUsers(userId: String):   Flow<Response<List<User>>>
    fun getPostId(): String
    fun setUserData(userObj: Map<String,String>,userId: String): Task<Void>
    fun  getUserPosts(uid: String): Flow<Response<List<Post>>>
    //    fun getUserId():String
    fun sentMessage(senderRoom: String, receiverRoom : String, message: Message): Flow<Response<Boolean>>
    fun getMessage(senderRoom: String): Flow<Response<List<Message>>>
    fun getAllPosts(): Flow<Response<List<Post>>>
    suspend fun  getLastMessage (senderRoom: String) : List<Message?>
    fun saveChat(chat: ChatList,   receiverId: String, senderId: String): Task<Void>
    fun getAllChat(senderId: String): Flow<Response<List<ChatList>>>
    fun setSubscriptionData(
        userObj : Map<String,List<Map<String, Boolean>>>,
        userId: String
    ): Task<Void>
    fun setFollowing (userId: String,  childUserId : String, following: Following): Flow<Response<Boolean>>
    fun setFollowers (userId: String,childUserId: String, followers: Followers): Flow<Response<Boolean>>
    fun getFollowing(userId: String): Flow<Response<List<Following>>>
    fun getFollowers(userId: String) : Flow<Response<List<Followers>>>
    fun unFollow(userId: String, childUserId: String):   Flow<Response<Boolean>>
    suspend fun isFollow(userId: String, childId: String): Following?
    fun setComments (postId: String, comment: Comment): Flow<Response<Boolean>>
    fun getComments(postId: String) : Flow<Response<List<Comment>>>
    fun updatePost(
        postObj : Map<String,ArrayList<String>>,
        postId: String
    ): Task<Void>
  fun setLike (postId: String, user: User): Flow<Response<Boolean>>
    fun getLike(postId: String) : Flow<Response<List<User>>>
    fun unLike(postId: String, userId: String) :  Flow<Response<Boolean>>
    suspend fun getPostById (postId : String): Post
    fun isLiked(postId: String,user: User): Flow <Response<Boolean>>
    suspend fun getAuthorLikeById(userId:String) : User
    fun setUpdatePostData(
        postObj : Map<String,  ArrayList<String>?>,
        postId: String
    ): Task<Void>
    fun updateFollow(
        userObj: Map<String, ArrayList<String>>,
        userId: String
    ): Task<Void>
    fun deletePost(postId: String): Task<Void>
  fun setNotification (receiverId: String, notification: Notification): Flow<Response<Boolean>>
  fun getNotification(receiverId: String) : Flow<Response<List<Notification>>>
  suspend fun searchUserByName(name: String): MutableList<User>
  suspend fun searchUserByLastName(lastName: String): MutableList<User>
}
class DatabaseFromFirebase @Inject constructor(
    private var databaseRef: DatabaseReference,
    private var db: FirebaseFirestore
) : Database {
    override fun getAllPosts():
        Flow<Response<List<Post>>> = callbackFlow{
            val snapshotListener = db.collection("posts")
                .addSnapshotListener{snapshot,e->
                    val response = if(snapshot!=null){
                        val postsList = snapshot.toObjects(Post::class.java)
                        Log.d("postsLists",postsList.toString())
                        Response.Success<List<Post>>(postsList)
                    }
                    else{
                        Response.Error(e?.message?:e.toString())
                    }
                    trySend(response).isSuccess
                }
            awaitClose {
                snapshotListener.remove()
            }
        }

    override suspend fun getLastMessage(senderRoom: String): List<Message?> {
        return  databaseRef.child("chats").child(senderRoom).child("message").orderByKey().limitToLast(1).get().await().children.map {
            it.getValue(Message::class.java)
        }
          }



    override fun getUserPosts(uid: String):  Flow<Response<List<Post>>> = callbackFlow{
        val snapshotListener = db.collection("posts").whereEqualTo("userId", uid)
            .addSnapshotListener{snapshot,e->
                val response = if(snapshot!=null){
                    val postsList = snapshot.toObjects(Post::class.java)
                    Log.d("postsLists",postsList.toString())
                    Response.Success<List<Post>>(postsList)
                }
                else{
                    Response.Error(e?.message?:e.toString())
                }
                trySend(response).isSuccess
            }
        awaitClose {
            snapshotListener.remove()
        }
    }

    override fun sentMessage(senderRoom: String, receiverRoom: String, message : Message): Flow<Response<Boolean>> = callbackFlow {
        Response.Success(false)
        val randomKey = databaseRef.push().key
        databaseRef.child("chats")
            .child(senderRoom).child("message").child(randomKey!!).setValue(message)
            .addOnSuccessListener {
                databaseRef.child("chats").child(receiverRoom).child("message")
                    .child(randomKey!!).setValue(message).addOnSuccessListener {
                        Response.Success(true)
                    }
            }
        awaitClose{
        }
    }

 override fun setFollowing (userId: String, childUserId : String,following: Following): Flow<Response<Boolean>> = callbackFlow{
     Response.Success(false)
     databaseRef.child("follow").child("following").child(userId).child(childUserId).setValue(following)
         .addOnSuccessListener{
             Response.Success(true)
         }
     awaitClose{
     }
 }
    override fun setFollowers (userId: String, childUserId: String, followers: Followers): Flow<Response<Boolean>> = callbackFlow{
        Response.Success(false)

        databaseRef.child("follow").child("followers").child(userId).child(childUserId).setValue(followers)
            .addOnSuccessListener{
                Response.Success(true)
            }
        awaitClose{
        }
    }
    override fun setComments (postId: String, comment: Comment): Flow<Response<Boolean>> = callbackFlow{
        Response.Success(false)
        val randomKey = databaseRef.push().key
        databaseRef.child("comments").child(postId).child(randomKey!!).setValue(comment)
            .addOnSuccessListener {
                Response.Success(true)
            }
        awaitClose{
        }
    }
    override fun setLike (postId: String, user: User): Flow<Response<Boolean>> = callbackFlow{
        Response.Success(false)

        databaseRef.child("likes").child(postId).child(user.userId).setValue(user)
            .addOnSuccessListener {
                Response.Success(true)
            }
        awaitClose{
        }
    }
    override fun getLike(postId: String) : Flow<Response<List<User>>> = callbackFlow {
        val likesListener= object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val comments = mutableListOf<User>()
                for (snapshot1 in snapshot.children ){
                    comments.add(
                        User(
                            snapshot1.child("userId").getValue(String::class.java)!!,
                            snapshot1.child("password").getValue(String::class.java)!!,
                            snapshot1.child("email").getValue(String::class.java)!!,
                            snapshot1.child("name").getValue(String::class.java)!!,
                            snapshot1.child("lastName").getValue(String::class.java)!!,
                            snapshot1.child("nickName").getValue(String::class.java)!!,
                            snapshot1.child("imageUrl").getValue(String::class.java)!!,
                            snapshot1.child("description").getValue(String::class.java)!!,
                            snapshot1.child("nFollowers").value as ArrayList<String>,
                            snapshot1.child("nFollowing").value as ArrayList<String>,
                        )
                    )
                    val   response = Response.Success(comments)
                    trySend(response).isSuccess
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Response.Error(error.message?:error.toString())
            }
        }
        databaseRef.child("likes").child(postId)
            .addValueEventListener(likesListener)
        awaitClose{
            databaseRef.removeEventListener(likesListener)
        }
    }
    override fun getComments(postId: String) : Flow<Response<List<Comment>>> = callbackFlow {
        val commentsListener= object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val comments = mutableListOf<Comment>()
                for (snapshot1 in snapshot.children ){
                    comments.add(
                        Comment(
                            snapshot1.child("author").getValue(User::class.java)!!,
                            snapshot1.child("text").getValue(String::class.java)?: "",
                            snapshot1.child("time").getValue(String::class.java)?: ""

                        )
                    )
                    val   response = Response.Success(comments)
                    trySend(response).isSuccess
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Response.Error(error.message?:error.toString())
            }
        }
        databaseRef.child("comments").child(postId)
            .addValueEventListener(commentsListener)
        awaitClose{
            databaseRef.removeEventListener(commentsListener)
        }
    }
    override fun getFollowers(userId: String) : Flow<Response<List<Followers>>> = callbackFlow{
        val followersListener = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val followers = mutableListOf<Followers>()
                for (snapshot1 in snapshot.children){
                    followers.add(
                        Followers(
                            snapshot1.child("user").getValue(User::class.java),
                            snapshot1.child("userId").getValue(String::class.java)!!
                        )
                    )
                    val   response = Response.Success(followers)
                    trySend(response).isSuccess
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Response.Error(error.message?:error.toString())
            }

        }
        databaseRef.child("follow").child("followers").child(userId)
            .addValueEventListener(followersListener)
        awaitClose{
            databaseRef.removeEventListener(followersListener)
        }

}
    override fun getFollowing(userId: String): Flow<Response<List<Following>>> = callbackFlow{
        val followingListener = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val following = mutableListOf<Following>()
                for (snapshot1 in snapshot.children){
                    following.add(
                        Following(
                            snapshot1.child("user").getValue(User::class.java),
                            snapshot1.child("isFollow").getValue(Boolean::class.java)?: false,
                            snapshot1.child("userId").getValue(String::class.java)!!


                        )
                    )
                    val   response = Response.Success(following)
                    trySend(response).isSuccess
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Response.Error(error.message?:error.toString())
            }

        }
        databaseRef.child("follow").child("following").child(userId)
            .addValueEventListener(followingListener)
        awaitClose{
            databaseRef.removeEventListener(followingListener)
        }

    }
    override fun unFollow(userId: String, childUserId: String):  Flow<Response<Boolean>> = callbackFlow {
        Response.Success(false)
        databaseRef.child("follow").child("following").child(userId).child(childUserId).removeValue().addOnSuccessListener {
            databaseRef.child("follow").child("followers").child(childUserId).child(userId).removeValue()
                .addOnSuccessListener {
                    Response.Success(true)
                }
        }
        awaitClose{

        }
    }
    override fun unLike(postId: String, userId: String) :  Flow<Response<Boolean>> = callbackFlow {
        Response.Success(false)
        databaseRef.child("likes").child(postId).child(userId).removeValue().addOnSuccessListener {
            Response.Success(true)
        }
        awaitClose{
        }
    }
        override suspend fun isFollow(userId: String, childId: String): Following? {
            return databaseRef.child("follow").child("following").child(userId).child(childId).get().await().getValue(Following::class.java)

        }
    override fun getMessage(senderRoom: String):  Flow<Response<List<Message>>> = callbackFlow{
        val messageListener = object : ValueEventListener{
    override fun onDataChange(snapshot: DataSnapshot) {
        val messages = mutableListOf<Message>()
        for (snapshot1 in snapshot.children){
         messages.add(
             Message(
                 snapshot1.child("message").getValue(String::class.java),
                 snapshot1.child("senderId").getValue(String::class.java),
                 snapshot1.child("receiverId").getValue(String::class.java),
                 snapshot1.child("time").getValue(String::class.java)
             )
         )
       val   response = Response.Success(messages)
            trySend(response).isSuccess
        }
    }
    override fun onCancelled(error: DatabaseError) {
        Response.Error(error.message?:error.toString())
    }
}
        databaseRef.child("chats").child(senderRoom).child("message")
            .addValueEventListener(messageListener)

awaitClose{
    databaseRef.removeEventListener(messageListener)
}
    }
    override fun isLiked(postId: String,user: User ): Flow <Response<Boolean>> = callbackFlow{
        val likedListener = object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.getValue()!=null){
                    val   response = Response.Success(true)
                    trySend(response).isSuccess
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        }
        databaseRef.child("likes").child(postId).child(user.userId).addListenerForSingleValueEvent(likedListener)
   awaitClose{

   }
    }

    override suspend fun setUserDataInfoOnDatabase(user: User) {
        db.collection("users")
            .document(user.userId).set(user)
    }
    override suspend fun setPostDataInfoOnDatabase(post: Post) : Task<Void> {
       val postId = db.collection("posts").document().id
        return  db.collection("posts")
            .document(post.postId?:postId ).set(post)
    }

    override fun saveChat(
        chat: ChatList,
         receiverId: String,
        senderId: String
    ): Task<Void> {
        return  db.collection("chatList")
            .document(receiverId+ senderId).set(chat)
    }

    override fun getAllChat(senderId: String): Flow<Response<List<ChatList>>> = callbackFlow{
            val snapshotListener = db.collection("chatList").whereEqualTo("senderId", senderId)
                .addSnapshotListener{snapshot,e->
                    val response = if(snapshot!=null){
                        val chatList = snapshot.toObjects(ChatList::class.java)
                        Response.Success<List<ChatList>>(chatList)
                    }
                    else{
                        Response.Error(e?.message?:e.toString())
                    }
                    trySend(response).isSuccess
                }
            awaitClose {
                snapshotListener.remove()
            }
        }

override suspend fun getPostById (postId : String): Post {
    return db.collection("posts").document(postId).get().await().toObject(Post::class.java)!!
        }


    override suspend fun getCurrentUserData(userId: String): User? {
        return db.collection("users").document(userId).get().await().toObject(User::class.java)
    }
    override suspend fun searchUserByName(name: String): MutableList<User> {
        return db.collection("users").whereEqualTo("name", name).get().await().toObjects(User::class.java)
    }
    override suspend fun searchUserByLastName(lastName: String): MutableList<User> {
        return db.collection("users").whereEqualTo("lastName", lastName).get().await().toObjects(User::class.java)
    }

    override fun getAllUsers(userId: String):
            Flow<Response<List<User>>> = callbackFlow{
        val snapshotListener = db.collection("users").whereNotEqualTo("userId", userId)
            .addSnapshotListener{snapshot,e->
                val response = if(snapshot!=null){
                    val usersList = snapshot.toObjects(User::class.java)
                    Log.d("usersLists",usersList.toString())
                    Response.Success<List<User>>(usersList)
                }
                else{
                    Response.Error(e?.message?:e.toString())
                }
                trySend(response).isSuccess
            }
        awaitClose {
            snapshotListener.remove()
        }
    }
    override fun getPostId(): String {
        return db.collection("posts").document().id
    }
    override fun setUserData(
        userObj : Map<String,String>,
        userId: String
    ): Task<Void> {
        return db.collection("users").document(userId).update(userObj)
    }
    override fun setSubscriptionData(
        userObj : Map<String,List<Map<String, Boolean>>>,
        userId: String
    ): Task<Void>{
        return db.collection("users").document(userId).update(userObj)
    }
    override fun updatePost(
        postObj : Map<String, ArrayList<String>>,
        postId: String
    ): Task<Void>{
        return db.collection("posts").document(postId).update(postObj)
    }
    override fun setUpdatePostData(
        postObj : Map<String,  ArrayList<String>?>,
        postId: String
    ): Task<Void>{
        return db.collection("posts").document(postId).update(postObj)
    }
   override suspend fun getAuthorLikeById(userId:String) : User {
       return db.collection("users").document(userId).get().await().toObject(User::class.java)!!
   }
    override fun updateFollow(
        userObj: Map<String, ArrayList<String>>,
        userId: String
    ): Task<Void> {
        return db.collection("users").document(userId).update(userObj)
    }
    override fun deletePost(postId: String): Task<Void> {
        return db.collection("posts").document(postId).delete()
    }
    override fun setNotification (receiverId: String, notification: Notification): Flow<Response<Boolean>> = callbackFlow{
        Response.Success(false)
        val randomKey = databaseRef.push().key
        databaseRef.child("notification").child(receiverId).child(randomKey!!).setValue(notification)
            .addOnSuccessListener {
                Response.Success(true)
            }
        awaitClose{
        }
    }
    override fun getNotification(receiverId: String) : Flow<Response<List<Notification>>> = callbackFlow {
        val notificationListener= object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val notification = mutableListOf<Notification>()
                for (snapshot1 in snapshot.children ){
                    notification.add(
                        Notification(
                            snapshot1.child("text").getValue(String::class.java)?: "",
                        )
                    )
                    val   response = Response.Success(notification)
                    trySend(response).isSuccess
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Response.Error(error.message?:error.toString())
            }
        }
        databaseRef.child("notification").child(receiverId)
            .addValueEventListener(notificationListener)
        awaitClose{
            databaseRef.removeEventListener(notificationListener)
        }
    }

}



