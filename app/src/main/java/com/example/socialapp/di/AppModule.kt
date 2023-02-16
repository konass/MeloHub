package com.example.socialapp.di

import android.content.Context
import androidx.room.Room
import com.example.socialapp.data.firebaseDatabase.Database
import com.example.socialapp.data.firebaseDatabase.DatabaseFromFirebase
import com.example.socialapp.data.firebaseDatabase.MusicDatabase
import com.example.socialapp.data.firebaseStorage.Storage
import com.example.socialapp.data.firebaseStorage.StorageByFireBase
import com.example.socialapp.data.local.db.SongDao
import com.example.socialapp.data.local.db.SongDatabase
import com.example.socialapp.data.remote.MusicApi
import com.example.socialapp.data.repository.*
import com.example.socialapp.domain.repository.*
import com.example.socialapp.domain.repository.MusicRepository
import com.example.socialapp.domain.use_case.MessageUseCases.*
import com.example.socialapp.domain.use_case.authenticationUseCases.*
import com.example.socialapp.domain.use_case.chatUseCases.*
import com.example.socialapp.domain.use_case.commentUseCases.*
import com.example.socialapp.domain.use_case.musicUseCases.*
import com.example.socialapp.domain.use_case.notificationUseCases.GetNotificationUseCase
import com.example.socialapp.domain.use_case.notificationUseCases.NotificationUseCases
import com.example.socialapp.domain.use_case.notificationUseCases.SetNotificationUseCase
import com.example.socialapp.domain.use_case.peopleUseCases.*
import com.example.socialapp.domain.use_case.peopleUseCases.GetCurrentUser
import com.example.socialapp.domain.use_case.postUseCases.*
import com.example.socialapp.domain.use_case.userUseCases.*
import com.example.socialapp.domain.use_case.userUseCases.SetUpdateUserPostData
import com.example.socialapp.exoplayer.MusicServiceConnection
import com.example.socialapp.presentation.apadters.SwipeAdapter
import com.example.socialapp.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideFirebaseAuthentication(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }
    @Singleton
    @Provides
    fun provideDatabaseReference() = FirebaseDatabase.getInstance().getReference()
    @Singleton
    @Provides
    fun provideFirebaseStorage() = FirebaseStorage.getInstance().reference
    @Singleton
    @Provides
    fun provideDatabase(
        databaseRef: DatabaseReference,
        db: FirebaseFirestore
    ): Database = DatabaseFromFirebase(databaseRef, db)
    @Singleton
    @Provides
    fun provideStorage(
        storageRef: StorageReference,
        database: Database
    ): Storage= StorageByFireBase(storageRef,database)
    @Provides
    @Singleton
    fun provideFirebaseFirestore() = FirebaseFirestore.getInstance()
    @Singleton
    @Provides
    fun provideAuthenticationRepository(auth:FirebaseAuth, database: Database): AuthenticationRepository {
        return AuthenticationRepositoryImpl(auth = auth, database = database)
    }
    @Singleton
    @Provides
    fun provideAuthUseCase(repository: AuthenticationRepository) = AuthenticationUseCases(
        firebaseIsUserAuthenticated = FirebaseIsUserAuthenticated(repository = repository),
        firebaseAuthState = FirebaseAuthState(repository = repository),
        firebaseSignOut = FirebaseSignOut(repository = repository),
        firebaseSignIn = FirebaseSignIn(repository = repository),
        firebaseSignUp = FirebaseSignUp(repository = repository),
        getUserId = GetUserId(repository=repository)
    )
    @Singleton
    @Provides
    fun postRepository( database: Database, auth: FirebaseAuth, storage: Storage): PostRepository {
        return PostRepositoryImpl( database = database, auth=auth, storage= storage)
    }
    @Singleton
    @Provides
    fun userRepository( database: Database, auth: FirebaseAuth, storage: Storage): UserRepository {
        return UserRepositoryImpl( database = database, auth=auth, storage= storage)
    }
    @Singleton
    @Provides
    fun peopleRepository( database: Database, auth: FirebaseAuth): PeopleRepository {
        return PeopleRepositoryImpl( database = database, auth=auth)
    }
    @Singleton
    @Provides
    fun providePostUseCase(repository: PostRepository) = PostUseCases(
        setPostDataOnDatabase = SetPostDataOnDatabase(repository = repository),
        uploadImage = UploadImage(repository=repository),
        getPostsUseCase = GetPostsUseCase(repository=repository),
        getPostAuthor = GetPostAuthor(repository=repository),
        updateLike = UpdateLike(repository=repository),
        setLike = SetLike(repository=repository),
        getLike = GetLike(repository=repository),
        unLike = UnLike(repository=repository),
        getPostById = GetPostById(repository= repository),
        isLiked = IsLiked(repository=repository),
        getAuthorLikeById = GetAuthorLikeById(repository=repository),
        getAuthorLikeId = GetAuthorLikeId(repository =repository),
        setUpdatePostData = SetUpdatePostData(repository = repository),
        getCurrentUserUseCase = GetCurrentUserUseCase(repository=repository),
        setNotificationLikeUseCase = SetNotificationLikeUseCase(repository=repository)
    )
    @Singleton
    @Provides
    fun provideUserUseCase(repository: UserRepository) = UserUseCases(
       getCurrentUserData = GetCurrentUserData(repository = repository),
        setUserData = SetUserData(repository=repository),
        uploadUserImage = UploadUserImage(repository=repository),
        getUserPosts = GetUserPosts(repository = repository),
        signOut = SignOut(repository=repository),
        getUserId = GetCurrentUserId(repository=repository),
        getUserFollowers = GetUserFollowers(repository=repository),
        getUserFollowing = GetUserFollowing(repository=repository),
        getUserLikeId = GetUserLikeId(repository=repository),
        getUserPostById = GetUserPostById(repository=repository),
        setUserUpdatePostData = SetUpdateUserPostData(repository = repository),
        deletePostUseCase = DeletePostUseCase(repository= repository)

    )
    @Singleton
    @Provides
    fun providePeopleUseCase(repository: PeopleRepository) = PeopleUseCases(
      getAllUsers = GetAllUsers(repository=repository),
        getUserData = GetUserData(repository=repository),
        getPeoplePosts = GetPeoplePosts(repository=repository),
        setFollowerData = SetFollowerData(repository=repository),
        setFollowers = SetFollowers(repository= repository),
        setFollowing = SetFollowing(repository= repository),
        getCurrentUser = GetCurrentUser(repository=repository),
        getFollowers = GetFollowers(repository=repository),
        getFollowing = GetFollowing(repository=repository),
        unfollow = Unfollow(repository = repository),
        isFollow = IsFollow(repository=repository),
        setUpdatePeoplePostData = SetUpdatePeoplePostData(repository =repository),
        getPeopleLikeId = GetPeopleLikeId(repository=repository),
        getPeoplePostById = GetPeoplePostById(repository=repository),
        getPeopleUserId = GetPeopleUserId(repository=repository),
        updateFollowers = UpdateFollowers(repository=repository),
        updateFollowing = UpdateFollowing(repository=repository),
        searchUserByName = SearchUserByName(repository = repository),
        searchUserByLastName= SearchUserByLastName(repository=repository)
    )
    @Singleton
    @Provides
    fun messageRepository( database: Database,auth: FirebaseAuth ): MessageRepository {
        return MessageRepositoryImpl( database = database,auth=auth)
    }
    @Singleton
    @Provides
    fun provideMessageUseCase(repository: MessageRepository) = MessageUseCases(
        sentMessage = SentMessage(repository=repository),
        getSenderId = GetSenderId(repository=repository),
        getMessages = GetMessages(repository=repository),
        getUser= GetUser(repository=repository),
        getLatestMessage = GetLatestMessage(repository=repository),
        getReceiverData = GetReceiverData(repository=repository),
        saveChat = SaveChat(repository=repository)
    )
    @Singleton
    @Provides
    fun chatRepository( database: Database,auth: FirebaseAuth): ChatRepository{
        return ChatRepositoryImpl(database=database, auth=auth)
    }
    @Singleton
    @Provides
    fun provideChatUseCases(repository: ChatRepository)= ChatUseCases(
        getALlChats = GetALlChats(repository=repository),
        getReceiver = GetReceiver(repository=repository),
        getSender = GetSender(repository=repository)
    )
    @Singleton
    @Provides
    fun provideCommentRepository(database: Database, auth: FirebaseAuth): CommentRepository{
        return CommentRepositoryImpl(database=database, auth)
    }
    @Singleton
    @Provides
    fun provideCommentUseCases(repository: CommentRepository)= CommentUseCases(
        setComment = SetComment(repository= repository),
        getComments= GetComments(repository=repository),
        getAuthor = GetAuthor(repository=repository),
        updateComment = UpdateComment(repository=repository),
        getPostForCommentById = GetPostForCommentById(repository=repository)
    )
    @Provides
    fun logging() = HttpLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.BODY)
    @Provides
    fun okHttpClient() = okhttp3.OkHttpClient.Builder()
        .addInterceptor(logging())
        .build()
    @Singleton
    @Provides
    fun provideMusicApi() : MusicApi {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MusicApi::class.java)
    }
    @Singleton
    @Provides
    fun provideMusicRepository(musicApi: MusicApi, songDao: SongDao): MusicRepository {
        return MusicRepositoryImpl(musicApi, songDao)
    }
    @Singleton
    @Provides
    fun provideMusicUseCases( repository: MusicRepository)= MusicUseCases(
        getAlbumInfoUseCase = GetAlbumInfoUseCase(repository=repository),
        getArtistsUseCase = GetArtistsUseCase(repository=repository),
        getChartTrackUseCase = GetChartTrackUseCase(repository=repository),
        getSearchArtistsUseCase = GetSearchArtistsUseCase(repository =repository),
        getTopAlbumsUseCase = GetTopAlbumsUseCase(repository=repository),
        getTrackInfoUseCase = GetTrackInfoUseCase(repository=repository),
        getSearchTrackUseCase = GetSearchTrackUseCase(repository=repository),
        getTopArtists = GetTopArtistsUseCase(repository=repository),
        getFavoriteMusic = GetFavoriteMusic(repository=repository),
        saveFavoriteMusic = SaveFavoriteMusic(repository=repository),
        deleteFavoriteMusicUseCase = DeleteFavoriteMusicUseCase(repository=repository)
    )
    @Singleton
    @Provides
    fun provideSwipeAdapter() = SwipeAdapter()
    @Singleton
    @Provides
    fun provideMusicServiceConnection(
        @ApplicationContext context: Context
    ) = MusicServiceConnection(context)
    @Provides
    @Singleton
    fun provideMusicDatabase(@ApplicationContext context: Context)=
        Room.databaseBuilder(
            context,
            SongDatabase::class.java,
            "music_database"
        ).build()
    @Provides
    fun provideMusicDao(appDatabase: SongDatabase): SongDao {
        return appDatabase.getSongDao()
    }
    @Singleton
    @Provides
    fun provideNotificationRepository(database: Database) : NotificationRepository{
        return NotificationRepositoryImpl (db=database)
    }
    @Singleton
    @Provides
    fun provideNotificationUseCases(repository: NotificationRepository) = NotificationUseCases(
        setNotificationUseCase = SetNotificationUseCase(repository = repository),
        getNotificationUseCase = GetNotificationUseCase(repository = repository)
    )
}
