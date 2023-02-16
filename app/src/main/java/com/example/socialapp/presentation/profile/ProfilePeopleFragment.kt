package com.example.socialapp.presentation.profile


import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.socialapp.R
import com.example.socialapp.databinding.FragmentProfilePeopleBinding
import com.example.socialapp.domain.models.*
import com.example.socialapp.presentation.apadters.ProfilePeopleAdapter
import com.example.socialapp.presentation.apadters.ProfilePeopleItemClickListener
import com.example.socialapp.presentation.notifications.NotificationViewModel
import com.example.socialapp.utils.Response
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_profile_people.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class ProfilePeopleFragment : Fragment(), ProfilePeopleItemClickListener {
    lateinit var user: User
    private val bundleArgs by navArgs<ProfilePeopleFragmentArgs>()
    private val viewModel by viewModels<ProfilePeopleViewModel>()
    lateinit var postAdapter: ProfilePeopleAdapter
    private lateinit var postList: List<Post?>
    private val notificationViewModel by viewModels<NotificationViewModel>()
    private var notification : Notification? = null
    private var followersUser: Followers? = null
    private var followingUser: Following? = null
    lateinit var currentUser: User
    lateinit var binding: FragmentProfilePeopleBinding
//    var countOfFollowers = 0
//    var countOfFollowing = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentProfilePeopleBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        user = bundleArgs.user
        initPostAdapter(this, viewModel)
        binding.rvPostsUserP.visibility = View.VISIBLE
        viewModel.getUserData(user.userId)
        getUserPosts(user.userId)
        lifecycleScope.launchWhenStarted {
            viewModel.getUserState.collect {
                when (it) {
                    is Response.Success -> {
                        user = it.data!!
                        tv_name_p.text = user.name
                        tv_last_name_p.text = user.lastName
                        tv_desc_p.text = user.description
                        count_of_following_p.text = it.data!!.nfollowing.size.toString()
                        count_of_followers_p.text = it.data!!.nfollowers.size.toString()
//                        getFollowing(user.userId)
//                      getFollowers(user.userId)

                        Glide.with(this@ProfilePeopleFragment).load(user.imageUrl).into(im_avatar_p)
                    }
                    is Response.Error -> {

                        Snackbar.make(
                            requireView(), "error" + it.message, Snackbar.LENGTH_LONG
                        ).show()

                    }
                    is Response.Loading -> {
                        Log.e("ProfileFragment", "loading")
                    }
                }
            }
        }
        binding.tabProfileP.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        binding.rvPostsUserP.visibility = View.VISIBLE
                    }
                    else -> {
                        Toast.makeText(requireContext(), " no selected", Toast.LENGTH_LONG).show()
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })
        binding.btnMessage.setOnClickListener {
            val bundle = bundleOf("receiverUid" to user)
            view.findNavController().navigate(
                R.id.action_profilePeopleFragment_to_messagingFragment,
                bundle
            )
        }
        binding.countOfFollowersP.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("userIdToFollow", user.userId)
            bundle.putString("type", "followers")
            view.findNavController().navigate(
                R.id.action_profilePeopleFragment_to_followFragment,
                bundle
            )
        }
        binding.countOfFollowingP.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("userIdToFollow", user.userId)
            bundle.putString("type", "following")
            view.findNavController().navigate(
                R.id.action_profilePeopleFragment_to_followFragment,
                bundle
            )
        }
        getCurrentUser()
    }

    private fun initPostAdapter(listener: ProfilePeopleItemClickListener, viewModel: ProfilePeopleViewModel) {
        postAdapter = ProfilePeopleAdapter(listener, viewModel)
        binding.rvPostsUserP.apply {
            adapter = postAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun getUserPosts(uid: String) {
        viewModel.getPosts(uid)
        lifecycleScope.launchWhenStarted {
            viewModel.getPostState.collect {
                when (it) {
                    is Response.Success -> {
                        postList = it.data
                        postAdapter.differ.submitList(postList)

                    }
                    is Response.Error -> {

                        Snackbar.make(
                            requireView(), "error" + it.message, Snackbar.LENGTH_LONG
                        ).show()

                    }
                    is Response.Loading -> {
                        Log.e("ProfileFragment", "loading")
                    }
                }
            }
        }
    }

    private fun setFollowers(user: User,childUser: User ,followers: Followers) {
        viewModel.setFollowers(user.userId,childUser.userId, followers)
        lifecycleScope.launchWhenStarted {
            viewModel.setFollowersState.collect {
                when (it) {
                    is Response.Success -> {
                       user.nfollowers.add(childUser.userId)
                        viewModel.updateFollowers(user.nfollowers, user.userId)
                        binding.countOfFollowersP.text = user.nfollowers.size.toString()
                    }
                    is Response.Error -> {

                        Snackbar.make(
                            requireView(), "error" + it.message, Snackbar.LENGTH_LONG
                        ).show()

                    }
                    is Response.Loading -> {
                        Log.e("ProfileFragment", "loading")
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setFollowing(user: User, childUser: User, following: Following) {
        viewModel.setFollowing(user.userId, childUser.userId, following)
        lifecycleScope.launchWhenStarted {
            viewModel.setFollowingState.collect {
                when (it) {
                    is Response.Success -> {
                        user.nfollowing.add(childUser.userId)
                        viewModel.updateFollowing(user.nfollowing, user.userId)
                        setNotification("${user.name} ${user.lastName} subscribed to you at ${LocalDateTime.now().format(DateTimeFormatter.ofPattern("H:m:ss"))}", childUser.userId!! )
                    }
                    is Response.Error -> {

                        Snackbar.make(
                            requireView(), "error" + it.message, Snackbar.LENGTH_LONG
                        ).show()

                    }
                    is Response.Loading -> {
                        Log.e("ProfileFragment", "loading")
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setFollow(senderUser: User, receiverUser: User) {
        binding.btnFollow.setOnClickListener {
            followersUser = Followers(senderUser, receiverUser.userId)
            followingUser = Following(receiverUser, true, senderUser.userId)
            setFollowers(receiverUser, senderUser, followersUser!!)
            setFollowing(senderUser, receiverUser, followingUser!!)
            binding.btnFollow.visibility= GONE
            binding.btnUnfollow.visibility= VISIBLE

        }

    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun getCurrentUser() {
        viewModel.getUser()
        lifecycleScope.launchWhenStarted {
            viewModel.getCurrentUserState.collect {
                when (it) {
                    is Response.Success -> {
                        currentUser = it.data!!
                        if (currentUser.nfollowing.contains(user.userId)){
                            binding.btnFollow.visibility= GONE
                            binding.btnUnfollow.visibility= VISIBLE
                        }
                        else {
                            binding.btnFollow.visibility= VISIBLE
                            binding.btnUnfollow.visibility= GONE
                        }
                        setFollow(currentUser, user)
                        unFollow(currentUser,user)
                        isFollow(currentUser.userId, user.userId)
                    }
                    is Response.Error -> {

                        Snackbar.make(
                            requireView(), "error" + it.message, Snackbar.LENGTH_LONG
                        ).show()

                    }
                    is Response.Loading -> {
                        Log.e("ProfileFragment", "loading")
                    }
                }

            }
        }
    }
//    private fun getFollowers(userId: String) {
//        viewModel.getFollowers(userId)
//        lifecycleScope.launchWhenStarted {
//            viewModel.getFollowersState.collect {
//                viewModel.getFollowersState.collect {
//                    when (it) {
//                        is Response.Success -> {
//                            countOfFollowers=it.data.size
//                            binding.countOfFollowersP.text = countOfFollowers.toString()
//                        }
//                        is Response.Error -> {
//
//                            Snackbar.make(
//                                requireView(), "error" + it.message, Snackbar.LENGTH_LONG
//                            ).show()
//
//                        }
//                        is Response.Loading -> {
//                            Log.e("Fragment", "loading")
//                        }
//
//                    }
//                }
//            }
//
//        }
//    }
//    private fun getFollowing(userId: String) {
//        viewModel.getFollowing(userId)
//        lifecycleScope.launchWhenStarted {
//            viewModel.getFollowersState.collect {
//                viewModel.getFollowingState.collect {
//                    when (it) {
//                        is Response.Success -> {
//                            countOfFollowing=it.data.size
//                            binding.countOfFollowingP.text = countOfFollowing.toString()
//                        }
//                        is Response.Error -> {
//                            Snackbar.make(
//                                requireView(), "error" + it.message, Snackbar.LENGTH_LONG
//                            ).show()
//
//                        }
//                        is Response.Loading -> {
//                            Log.e("Fragment", "loading")
//                        }
//
//                    }
//                }
//            }
//
//        }
//    }
    private fun unFollow(user: User, childUser: User){
binding.btnUnfollow.setOnClickListener {
    viewModel.unfollow(user.userId, childUser.userId)
    lifecycleScope.launchWhenStarted {
        viewModel.unfollowState.collect{
            when (it) {
                is Response.Success -> {
                    user.nfollowing.remove(childUser.userId)
                    childUser.nfollowers.remove(user.userId)
                    viewModel.updateFollowers(childUser.nfollowers, childUser.userId)
                    viewModel.updateFollowing(user.nfollowing, user.userId)
                 binding.btnFollow.visibility= VISIBLE
                    binding.btnUnfollow.visibility= GONE
                }
                is Response.Error -> {
                    Snackbar.make(
                        requireView(), "error" + it.message, Snackbar.LENGTH_LONG
                    ).show()

                }
                is Response.Loading -> {
                    Log.e("Fragment", "loading")
                }

            }
        }
    }
}
    }
    private fun isFollow(userId: String, childUserId: String){
        lifecycleScope.launchWhenStarted {
            followingUser= viewModel.isFollow(userId,   childUserId)
            if (followingUser?.isFollow ?: false){
                binding.btnFollow.visibility= GONE
                binding.btnUnfollow.visibility= VISIBLE
            }
            else{
                binding.btnFollow.visibility= VISIBLE
                binding.btnUnfollow.visibility= GONE
            }
        }
    }

    override fun onLikeClicked(postId: String) {
        viewModel.updateLike(postId)
    }

    override fun commentOnPost(post: Post) {
        val bundle = bundleOf("post" to post.postId!!)
        view?.findNavController()?.navigate(
            R.id.action_profileFragment_to_commentFragment,
            bundle
        )
    }
    fun setNotification( text: String, receiverId: String){
        notification = Notification (text)
        notificationViewModel.setNotification(receiverId, notification!!)
    }

}