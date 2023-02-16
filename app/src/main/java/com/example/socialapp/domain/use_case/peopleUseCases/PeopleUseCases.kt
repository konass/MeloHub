package com.example.socialapp.domain.use_case.peopleUseCases

data class PeopleUseCases (
    val getAllUsers: GetAllUsers,
    val getUserData: GetUserData,
    val getPeoplePosts: GetPeoplePosts,
    val setFollowerData: SetFollowerData,
    val setFollowers: SetFollowers,
    val setFollowing: SetFollowing,
    val getCurrentUser: GetCurrentUser,
    val getFollowers: GetFollowers,
    val getFollowing : GetFollowing,
    val unfollow: Unfollow,
    val isFollow: IsFollow,
    val getPeopleLikeId: GetPeopleLikeId,
    val getPeoplePostById: GetPeoplePostById,
    val setUpdatePeoplePostData: SetUpdatePeoplePostData,
    val getPeopleUserId: GetPeopleUserId,
    val updateFollowers: UpdateFollowers,
    val updateFollowing: UpdateFollowing,
    val searchUserByName: SearchUserByName,
    val searchUserByLastName: SearchUserByLastName
)