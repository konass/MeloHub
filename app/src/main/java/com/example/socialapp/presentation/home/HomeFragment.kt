package com.example.socialapp.presentation.home

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.socialapp.R
import com.example.socialapp.databinding.FragmentHomeBinding
import com.example.socialapp.domain.models.Post
import com.example.socialapp.domain.models.User
import com.example.socialapp.presentation.apadters.HomeItemClickListener
import com.example.socialapp.presentation.apadters.PostAdapter
import com.example.socialapp.utils.Response
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : Fragment(), HomeItemClickListener {
    lateinit var binding : FragmentHomeBinding
    private  lateinit var postList: List<Post?>
    var user: User?  = null

    var text: String? = null
    var post: Post? = null
    var postId : String = ""
    private val viewModel by viewModels<HomeViewModel>()
    lateinit var postAdapter: PostAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getPosts()
    initPostAdapter(this,viewModel)
       lifecycleScope.launchWhenCreated {
            viewModel.getPostState.collect{
                when(it){
                    is Response.Success -> {
                        Log.e("HomeFragment", ""+ it.data.size)
                        postList = it.data
                        postAdapter.differ.submitList(postList)
                        postAdapter.notifyDataSetChanged()
                    }
                    is Response.Error -> {

                        Snackbar.make(
                            requireView(), "error" + it.message, Snackbar.LENGTH_LONG
                        ).show()

                    }
                    is Response.Loading -> {
                        Log.e("HomeFragment", "loading" )
                    }
                }

            }
        }
        binding.people.setOnClickListener {
            view?.findNavController()?.navigate(
                R.id.action_homeFragment_to_peopleFragment,
            )
        }
        binding.notification.setOnClickListener {
            view?.findNavController()?.navigate(
                R.id.action_homeFragment_to_notificationsFragment,
            )
        }
    }
    private fun initPostAdapter(listener: HomeItemClickListener, viewModel: HomeViewModel) {
        postAdapter = PostAdapter(listener, viewModel)
     binding.rvPosts.apply {
            adapter = postAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onLikeClicked(post: Post) {
       viewModel.updateLike(post.postId!!)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun commentOnPost(post: Post) {
        val bundle = bundleOf("post" to post)
        view?.findNavController()?.navigate(
            R.id.action_homeFragment_to_commentFragment,
            bundle
        )
    }


}