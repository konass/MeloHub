package com.example.socialapp.presentation.profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.socialapp.R
import com.example.socialapp.databinding.FragmentFollowBinding
import com.example.socialapp.domain.models.Followers
import com.example.socialapp.domain.models.Following
import com.example.socialapp.presentation.apadters.FollowersAdapter
import com.example.socialapp.presentation.apadters.FollowingAdapter
import com.example.socialapp.utils.Response
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_follow.*


@AndroidEntryPoint
class FollowFragment : Fragment() {
   lateinit var userId : String
    lateinit var followersAdapter: FollowersAdapter
    lateinit var followingAdapter : FollowingAdapter
    lateinit var binding : FragmentFollowBinding
    lateinit var followersList: List<Followers>
    lateinit var followingList: List<Following>

    private val viewModel by viewModels<ProfilePeopleViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
      binding= FragmentFollowBinding.inflate(layoutInflater,  container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (arguments?.getString("type") == "following"){
           getFollowing(arguments?.getString("userIdToFollow").toString())
        }
        else {
            getFollowers(arguments?.getString("userIdToFollow").toString())
        }
    }
private fun getFollowers(userId: String){
    viewModel.getFollowers(userId)
    initFollowersAdapter()
    followersAdapter.setOnItemClickListener {
        val bundle = bundleOf("user" to it)
        view?.findNavController()?.navigate(
            R.id.action_followFragment_to_profilePeopleFragment,
            bundle
        )
    }
    lifecycleScope.launchWhenStarted {
        viewModel.getFollowersState.collect{
            when(it){
                is Response.Success->{
                    followersList= it.data
                    followersAdapter.differ.submitList(followersList)
                    followersAdapter.notifyDataSetChanged()
                    Log.e("FollowFragment", "submitList ${followersList.size}" )
                }
                is Response.Error -> {

                    Snackbar.make(
                        requireView(), "error" + it.message, Snackbar.LENGTH_LONG
                    ).show()

                }
                is Response.Loading -> {
                    Log.e("FollowFragment", "loading" )
                }

            }            }
        }
    }
    private fun getFollowing(userId: String){
        viewModel.getFollowing(userId)
        initFollowingAdapter()
        followingAdapter.setOnItemClickListener {
            val bundle = bundleOf("user" to it)
            view?.findNavController()?.navigate(
                R.id.action_followFragment_to_profilePeopleFragment,
                bundle
            )
        }
        lifecycleScope.launchWhenStarted {
            viewModel.getFollowingState.collect{
                when(it){
                    is Response.Success->{
                        followingList= it.data
                        followingAdapter.differ.submitList(followingList)
                        followingAdapter.notifyDataSetChanged()
                    }
                    is Response.Error -> {

                        Snackbar.make(
                            requireView(), "error" + it.message, Snackbar.LENGTH_LONG
                        ).show()

                    }
                    is Response.Loading -> {
                        Log.e("FollowFragment", "loading" )
                    }

                }            }
        }
    }
    private fun initFollowingAdapter(){
        followingAdapter = FollowingAdapter()
        rv_follow.apply {
            adapter = followingAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        }
    }
    private fun initFollowersAdapter(){
        followersAdapter = FollowersAdapter()
       rv_follow.apply {
            adapter = followersAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        }
    }


}