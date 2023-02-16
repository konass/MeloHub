package com.example.socialapp.domain.use_case.userUseCases

data class UserUseCases(
    val getCurrentUserData: GetCurrentUserData,
    val setUserData: SetUserData,
    val uploadUserImage: UploadUserImage,
    val getUserPosts: GetUserPosts,
    val signOut: SignOut,
   val  getUserId: GetCurrentUserId,
    val getUserFollowers: GetUserFollowers,
    val getUserFollowing: GetUserFollowing,
    val getUserLikeId: GetUserLikeId,
    val getUserPostById: GetUserPostById,
    val setUserUpdatePostData: SetUpdateUserPostData,
    val deletePostUseCase: DeletePostUseCase
)
