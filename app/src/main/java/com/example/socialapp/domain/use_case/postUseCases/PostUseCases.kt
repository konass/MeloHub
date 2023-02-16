package com.example.socialapp.domain.use_case.postUseCases

data class PostUseCases (
     val setPostDataOnDatabase: SetPostDataOnDatabase,
     val uploadImage: UploadImage,
     val getPostsUseCase: GetPostsUseCase,
     val getPostAuthor: GetPostAuthor,
     val updateLike: UpdateLike,
     val setLike: SetLike,
     val getLike: GetLike,
     val unLike: UnLike,
     val getPostById : GetPostById,
     val isLiked: IsLiked,
     val getAuthorLikeById: GetAuthorLikeById,
     val getAuthorLikeId: GetAuthorLikeId,
     val setUpdatePostData: SetUpdatePostData,
     val getCurrentUserUseCase: GetCurrentUserUseCase,
     val setNotificationLikeUseCase: SetNotificationLikeUseCase
        )